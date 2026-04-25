package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.PokemonDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.dto.pokemon.PokeListResponseDTO;
import com.digis.PokedexApi.entity.Pokemon;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiService {

    private static final String URL_POKEMON_API = "https://pokeapi.co/api/v2/pokemon/";
    private static final String LIST_URL = "https://pokeapi.co/api/v2/pokemon?limit={limit}&offset={offset}";
    private static final int DEFAULT_LIMIT = 20;

    @Autowired
    private RestTemplate pokemonRestTemplate;

    public Result getAll() {
        Result result = new Result();
        try {
            
            PokeListResponseDTO lista = pokemonRestTemplate.getForObject(LIST_URL, PokeListResponseDTO.class);
            
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getMessage();
            result.ex = e;
        }
        return result;
    }
}
