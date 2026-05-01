package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.PokemonApiResponseDTO;
import com.digis.PokedexApi.dto.PokemonDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.dto.pokemon.PokeListResponseDTO;
import com.digis.PokedexApi.mapper.PokemonMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.digis.PokedexApi.repository.PokemonApiRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@Service
public class PokeApiService extends BaseService {

    private static final String URL_ID = "https://pokeapi.co/api/v2/pokemon/";
    private static final String LIST_URL = "https://pokeapi.co/api/v2/pokemon?limit={limit}&offset={offset}";
    private static final String URL_BASE = "https://pokeapi.co/api/v2/pokemon?limit=1&offset=0";

    @Autowired
    private RestTemplate pokemonRestTemplate;
    @Autowired
    private PokemonApiRepository pokemonRepository;
    @Autowired
    private PokemonMapper mapper;
    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "pokemon-name", key = "#name")
    public Result getAllByName(String name) {
        Result result = new Result();

        try {
            PokemonDTO pokemon = fetchDetalle(URL_ID + name);
            if (pokemon == null) {
                result.correct = false;
                result.errorMessage = "No se encontro al pokemon con el nombre de " + name;
                return result;
            }
            result.object = pokemon;
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }

    @Cacheable(value = "pokemon-id", key = "#id")
    public Result getAllById(int id) {
        Result result = new Result();
        try {
            PokemonDTO pokemon = fetchDetalle(URL_ID + id);
            if (pokemon == null) {
                result.correct = false;
                result.errorMessage = "No se encontro el pokemon";
                return result;
            }
            result.object = pokemon;
            result.correct = true;

        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }

    @Cacheable(value = "pokemon-todos")
    public Result getAll() {
        Result result = new Result();
        try {
            PokeListResponseDTO pokemons = pokemonRestTemplate.getForObject(URL_BASE, PokeListResponseDTO.class);
            int total = pokemons.getCount();
            List<CompletableFuture<List<PokemonDTO>>> bloques = new ArrayList<>();
            for (int offset = 0; offset < total; offset += 100) {
                final int off = offset;
                bloques.add(CompletableFuture.supplyAsync(() -> fetchBloque(100, off)));
            }

            List<PokemonDTO> todos = bloques.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            result.objects = todos;
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }

    @Cacheable(value = "pokemon-paginado", key = "#limit + '-' + #offset")
    public Result getPaginado(int limit, int offset) {
        return ejecutarLista(() -> {
            PokeListResponseDTO respuesta = pokemonRestTemplate.getForObject(LIST_URL, PokeListResponseDTO.class, limit, offset);
            if (respuesta == null || respuesta.getResults().isEmpty()) {
                throw new RuntimeException("No se obtuvieron resultados");
            }
            return respuesta.getResults().stream().map(
                    item -> CompletableFuture.supplyAsync(() -> fetchDetalle(item.getUrl()))
            ).collect(Collectors.toList()).stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        });

    }

    private PokemonDTO fetchDetalle(String url) {
        try {
            return mapper.apiResponseToDTO(pokemonRestTemplate.getForObject(url, PokemonApiResponseDTO.class));
        } catch (Exception e) {
            return null;
        }
    }

    private List<PokemonDTO> fetchBloque(int limit, int offset) {
        PokeListResponseDTO lista_pokemon = pokemonRestTemplate.getForObject(LIST_URL, PokeListResponseDTO.class, limit, offset);
        if (lista_pokemon == null) {
            return List.of();
        }
        return lista_pokemon.getResults().stream().map(item -> fetchDetalle(item.getUrl())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public PokemonDTO getFromCacheById(int id) {
        Cache cache = cacheManager.getCache("pokemon-id");
        if (cache != null) {
            Cache.ValueWrapper cached = cache.get(id);
            if (cached != null) {
                Result result = (Result) cached.get();
                if (result != null && result.object != null) {
                    return (PokemonDTO) result.object;
                }
            }
        }
        Result result = getAllById(id);  // @Cacheable lo guarda solo
        return result.correct ? (PokemonDTO) result.object : null;
    }
}
