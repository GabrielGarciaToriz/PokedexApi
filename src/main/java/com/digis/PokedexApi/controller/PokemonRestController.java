package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pokemon")
public class PokemonRestController {

    @Autowired
    private PokeApiService pokeApiService;

    @GetMapping
    public ResponseEntity<Result> getAll(
            @RequestParam(defaultValue = "1349") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        Result result = pokeApiService.getPaginado(limit, offset);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/todos")
    public ResponseEntity<Result> getTodos() {
        Result result = pokeApiService.getAll();
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getAllById(@PathVariable("id") int id) {
        Result result = pokeApiService.getAllById(id);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Result> getAllByName(@PathVariable("name") String name) {
        Result result = pokeApiService.getAllByName(name);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
