package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.PokemonApiResponseDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.dto.pokemon.PokeListResponseDTO;
import com.digis.PokedexApi.dto.pokemon.StatDTO;
import com.digis.PokedexApi.entity.Pokemon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiService {

    private static final String URL_ID = "https://pokeapi.co/api/v2/pokemon/";
    private static final String LIST_URL = "https://pokeapi.co/api/v2/pokemon?limit={limit}&offset={offset}";
    private static final String URL_BASE = "https://pokeapi.co/api/v2/pokemon?limit=1&offset=0";

    @Autowired
    private RestTemplate pokemonRestTemplate;

    @Cacheable(value = "pokemon-name", key = "#name")
    public Result getAllByName(String name) {
        Result result = new Result();

        try {
            Pokemon pokemon = fetchDetalle(URL_ID + name);
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
            Pokemon pokemon = fetchDetalle(URL_ID + id);
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
            List<CompletableFuture<List<Pokemon>>> bloques = new ArrayList<>();
            for (int offset = 0; offset < total; offset += 100) {
                final int off = offset;
                bloques.add(CompletableFuture.supplyAsync(() -> fetchBloque(100, off)));
            }

            List<Pokemon> todos = bloques.stream()
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

    private List<Pokemon> fetchBloque(int limit, int offset) {
        PokeListResponseDTO lista_pokemon = pokemonRestTemplate.getForObject(LIST_URL, PokeListResponseDTO.class, limit, offset);
        if (lista_pokemon == null) {
            return List.of();
        }
        return lista_pokemon.getResults().stream().map(item -> fetchDetalle(item.getUrl())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Cacheable(value = "pokemon-paginado", key = "#limit + '-' + #offset")
    public Result getPaginado(int limit, int offset) {
        Result result = new Result();
        try {
            PokeListResponseDTO respuesta = pokemonRestTemplate.getForObject(LIST_URL, PokeListResponseDTO.class, limit, offset);
            if (respuesta == null || respuesta.getResults().isEmpty()) {
                result.correct = false;
                result.errorMessage = "No se obtuvieron resultados";
            }
            List<CompletableFuture<Pokemon>> futures = respuesta.getResults().stream()
                    .map(item -> CompletableFuture.supplyAsync(()
                    -> fetchDetalle(item.getUrl()))).collect(Collectors.toList());
            List<Pokemon> pokemons = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.objects = pokemons;
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getMessage();
            result.ex = e;
        }
        return result;
    }

    private Pokemon fetchDetalle(String url) {
        try {
            PokemonApiResponseDTO respuesta = pokemonRestTemplate.getForObject(url, PokemonApiResponseDTO.class);
            return mapearPokemon(respuesta);
        } catch (Exception e) {
            return null;
        }
    }

    private Pokemon mapearPokemon(PokemonApiResponseDTO pokemonRespuesta) {
        if (pokemonRespuesta == null) {
            return null;
        }
        Pokemon pokemon = new Pokemon();
        pokemon.setIdPokemon(pokemonRespuesta.getIdPokemon());
        pokemon.setName(pokemonRespuesta.getName());
        pokemon.setWeight(String.valueOf(pokemonRespuesta.getWeight()));
        pokemon.setHeight(String.valueOf(pokemonRespuesta.getHeight()));
        pokemon.setBase_expirence(String.valueOf(pokemonRespuesta.getBaseExperience()));
        pokemon.set_default(pokemonRespuesta.isDefault());
        List<String> types = pokemonRespuesta.getTypes().stream().map(t -> t.getType().getName()).collect(Collectors.toList());
        pokemon.setTypes(types);
        List<String> moves = pokemonRespuesta.getMoves().stream().map(m -> m.getMove().getName()).collect(Collectors.toList());
        pokemon.setMoves(moves);
        Map<String, Integer> stats = pokemonRespuesta.getStats().stream().collect(Collectors.toMap(s -> s.getStat().getName(), StatDTO::getBaseStat));
        pokemon.setStats(stats);
        pokemon.setSprites(pokemonRespuesta.getSprites());
        return pokemon;
    }

}
