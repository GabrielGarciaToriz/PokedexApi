package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.PokemonApiResponseDTO;
import com.digis.PokedexApi.dto.PokemonDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.dto.pokemon.PokeListResponseDTO;
import com.digis.PokedexApi.exception.ErrorCode;
import com.digis.PokedexApi.mapper.PokemonMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiService extends BaseService {

    private static final String URL_ID = "https://pokeapi.co/api/v2/pokemon/";
    private static final String LIST_URL = "https://pokeapi.co/api/v2/pokemon?limit={limit}&offset={offset}";
    private static final String URL_BASE = "https://pokeapi.co/api/v2/pokemon?limit=1&offset=0";

    @Autowired
    private RestTemplate pokemonRestTemplate;
    @Autowired
    private PokemonMapper mapper;

    // ─────────────────────────────────────────────────────────────
    // ÚNICO punto de contacto con la PokéAPI externa
    // ─────────────────────────────────────────────────────────────
    @Cacheable(value = "pokemon-todos")
    public Result getAll() {
        Result result = new Result();
        try {
            PokeListResponseDTO base = pokemonRestTemplate
                    .getForObject(URL_BASE, PokeListResponseDTO.class);
            int total = base.getCount();

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

    public Result getAllById(int id) {
        List<PokemonDTO> todos = getTodos();
        if (todos == null) {
            return errorCarga();
        }

        return todos.stream()
                .filter(p -> p.getIdPokemon() == id)
                .findFirst()
                .map(Result::ok)
                .orElseGet(() -> Result.error(ErrorCode.NOT_FOUND,
                "No se encontró el pokémon con id " + id));
    }

    public Result getAllByName(String name) {
        List<PokemonDTO> todos = getTodos();
        if (todos == null) {
            return errorCarga();
        }

        return todos.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(Result::ok)
                .orElseGet(() -> Result.error(ErrorCode.NOT_FOUND,
                "No se encontró el pokémon con nombre " + name));
    }

    public Result getPaginado(int limit, int offset) {
        List<PokemonDTO> todos = getTodos();
        if (todos == null) {
            return errorCarga();
        }

        List<PokemonDTO> pagina = todos.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        return pagina.isEmpty()
                ? Result.error(ErrorCode.NOT_FOUND, "Sin resultados para esa paginación")
                : Result.okList(pagina);
    }

    public Result buscar(Integer id, String nombre, String tipo) {
        List<PokemonDTO> todos = getTodos();
        if (todos == null) {
            return errorCarga();
        }

        List<PokemonDTO> filtrados = todos.stream()
                .filter(p -> id == null || p.getIdPokemon() == id)
                .filter(p -> nombre == null || p.getName().equalsIgnoreCase(nombre))
                .filter(p -> tipo == null || (p.getTypes() != null
                && p.getTypes().stream().anyMatch(t -> t.equalsIgnoreCase(tipo))))
                .collect(Collectors.toList());

        return filtrados.isEmpty()
                ? Result.error(ErrorCode.NOT_FOUND, "No se encontraron pokémon con esos filtros")
                : Result.okList(filtrados);
    }

    public PokemonDTO getFromCacheById(int id) {
        Result result = getAllById(id);
        return result.correct ? (PokemonDTO) result.object : null;
    }

    @SuppressWarnings("unchecked")
    private List<PokemonDTO> getTodos() {
        Result r = this.getAll(); 
        return r.correct ? (List<PokemonDTO>) r.objects : null;
    }

    private Result errorCarga() {
        return Result.error(ErrorCode.INTERNAL_ERROR, "Error al obtener la lista de pokémon");
    }

    private PokemonDTO fetchDetalle(String url) {
        try {
            return mapper.apiResponseToDTO(
                    pokemonRestTemplate.getForObject(url, PokemonApiResponseDTO.class));
        } catch (RestClientException e) {
            return null;
        }
    }

    private List<PokemonDTO> fetchBloque(int limit, int offset) {
        PokeListResponseDTO lista = pokemonRestTemplate
                .getForObject(LIST_URL, PokeListResponseDTO.class, limit, offset);
        if (lista == null) {
            return List.of();
        }
        return lista.getResults().stream()
                .map(item -> fetchDetalle(item.getUrl()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
