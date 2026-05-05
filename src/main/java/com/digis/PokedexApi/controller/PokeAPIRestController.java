package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pokemon")
public class PokeAPIRestController extends BaseController {

    @Autowired
    private PokeApiService pokeApiService;

    @GetMapping
    public ResponseEntity<Result> getAll(
            @RequestParam(defaultValue = "1349") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return responder(pokeApiService.getPaginado(limit, offset));
    }

    @GetMapping("/todos")
    public ResponseEntity<Result> getTodos() {
        return responder(pokeApiService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getAllById(@PathVariable("id") int id) {
       return responder(pokeApiService.getAllById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Result> getAllByName(@PathVariable("name") String name) {
        return responder(pokeApiService.getAllByName(name));
    }

}
