package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/catalogo")
@Tag(name = "Catalogo", description = "Tipos de pokemon y roles")
public class CatalogoRestController extends BaseController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(summary = "Obtener todos los tipos de pokemon", description = "Devolver la lista completa de todos los tipos de pokemons ")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "La lista fue obtenida con exito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipos")
    public ResponseEntity<Result> getAllTipos() {
        return responder(catalogoService.getAllTipos());
    }

    @Operation(summary = "Obtener todos los roles",
            description = "Devuelve los roles disponibles para asignar a usuarios (ADMIN, USER, etc.).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/rol")
    public ResponseEntity<Result> getAllRol() {
        return responder(catalogoService.getAllRol());
    }
}
