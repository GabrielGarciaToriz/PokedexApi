package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pokemon")
@Tag(name = "PokéAPI", description = "Consulta de pokémon desde la PokéAPI externa (con caché)")
public class PokeAPIRestController extends BaseController {

    @Autowired
    private PokeApiService pokeApiService;

    @Operation(summary = "Obtener pokémon paginados",
            description = "Consulta pokémon con paginación. Caché: `pokemon-paginado:{limit}-{offset}`.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista paginada obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se obtuvieron resultados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Result> getAll(
            @Parameter(description = "Número máximo de pokémon a retornar", example = "20")
            @RequestParam(defaultValue = "1349") int limit,
            @Parameter(description = "Registros a omitir para paginación", example = "0")
            @RequestParam(defaultValue = "0") int offset
    ) {
        return responder(pokeApiService.getPaginado(limit, offset));
    }

    @Operation(summary = "Obtener todos los pokémon",
            description = """
                   Consulta todos los pokémon de forma asíncrona (bloques de 100).
                   Resultado en caché: `pokemon-todos`.
                    Puede tardar varios segundos en la primera ejecución.
                   """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista completa obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/todos")
    public ResponseEntity<Result> getTodos() {
        return responder(pokeApiService.getAll());
    }

    @Operation(summary = "Obtener pokémon por ID",
            description = "Busca un pokémon por su ID numérico. Caché: `pokemon-id:{id}`.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon encontrado"),
        @ApiResponse(responseCode = "404", description = "Pokémon no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Result> getAllById(@Parameter(description = "ID numérico del pokémon (ej. 1 = Bulbasaur, 25 = Pikachu)", example = "25")
            @PathVariable("id") int id) {
        return responder(pokeApiService.getAllById(id));
    }

    @Operation(summary = "Obtener pokémon por nombre",
            description = "Busca un pokémon por su nombre en inglés (minúsculas). Caché: `pokemon-name:{name}`.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon encontrado"),
        @ApiResponse(responseCode = "404", description = "Pokémon no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Result> getAllByName(@Parameter(description = "Nombre del pokémon en minúsculas", example = "pikachu")
            @PathVariable("name") String name) {
        return responder(pokeApiService.getAllByName(name));
    }

}
