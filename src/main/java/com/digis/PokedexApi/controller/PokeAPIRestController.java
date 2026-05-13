package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pokemon")
@Tag(name = "PokéAPI", description = "Consulta de pokémon desde caché en memoria")
public class PokeAPIRestController extends BaseController {

    @Autowired
    private PokeApiService pokeApiService;

    @Operation(summary = "Todos los pokémon paginados")
    @GetMapping
    public ResponseEntity<Result> getAll(
            @RequestParam(defaultValue = "1349") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return responder(pokeApiService.getPaginado(limit, offset));
    }

    @Operation(summary = "Lista completa (carga inicial / warm-up del caché)")
    @GetMapping("/todos")
    public ResponseEntity<Result> getTodos() {
        return responder(pokeApiService.getAll());
    }

    @Operation(summary = "Buscar por ID, nombre y/o tipo",
            description = "Coincidencias exactas (case-insensitive). Al menos un parámetro es requerido.")
    @GetMapping("/buscar")
    public ResponseEntity<Result> buscar(
            @Parameter(description = "ID exacto del pokémon", example = "25")
            @RequestParam(required = false) Integer id,
            @Parameter(description = "Nombre exacto", example = "pikachu")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Tipo exacto (tipo1 o tipo2)", example = "electric")
            @RequestParam(required = false) String tipo) {
        return responder(pokeApiService.buscar(id, nombre, tipo));
    }

    @Operation(summary = "Pokémon por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Result> getAllById(@PathVariable int id) {
        return responder(pokeApiService.getAllById(id));
    }

    @Operation(summary = "Pokémon por nombre")
    @GetMapping("/name/{name}")
    public ResponseEntity<Result> getAllByName(@PathVariable String name) {
        return responder(pokeApiService.getAllByName(name));
    }
}
