package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/favorito")
@Tag(name = "Favoritos", description = "Gestión de pokémon favoritos por usuario")
public class PokemonRestController extends BaseController {

    @Autowired
    private PokemonService favoritoService;

    @Operation(summary = "Obtener favoritos de un usuario",
            description = "Devuelve la lista de pokémon marcados como favoritos por el usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de favoritos obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Result> getFavoritos(@Parameter(description = "ID del usuario", example = "1")
            @PathVariable("idUsuario") int idUsuario) {
        return responder(favoritoService.getFavoritoById(idUsuario));

    }

    @Operation(summary = "Verificar si un pokémon es favorito",
            description = "Retorna `true` si el pokémon ya está en la lista de favoritos del usuario.")
    @ApiResponse(responseCode = "200", description = "Verificación realizada")
    @GetMapping("/{idUsuario}/existe/{idPokemon}")
    public ResponseEntity<Result> esFavorito(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable int idUsuario,
            @Parameter(description = "ID del pokémon", example = "25")
            @PathVariable int idPokemon) {
        return responder(favoritoService.isFav(idUsuario, idPokemon));
    }

    @Operation(summary = "Agregar pokémon a favoritos",
            description = """
                   Agrega un pokémon a la lista de favoritos del usuario.
                   Se obtiene del caché (o de la PokéAPI si no está cacheado)
                   y se persiste en base de datos.
                   """)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon agregado a favoritos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pokémon no encontrado"),
        @ApiResponse(responseCode = "409", description = "El pokémon ya está en favoritos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{idUsuario}/pokemon/{idPokemon}")
    public ResponseEntity<Result> agregarFavorito(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable("idUsuario") int idUsuario,
            @Parameter(description = "ID del pokémon a agregar", example = "25")
            @PathVariable int idPokemon) {
        return responder(favoritoService.agregarFavoritoDesdeCache(idUsuario, idPokemon));
    }

    @Operation(summary = "Eliminar pokémon de favoritos",
            description = "Elimina un pokémon de la lista de favoritos del usuario indicado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pokémon eliminado de favoritos"),
        @ApiResponse(responseCode = "404", description = "El pokémon no está en favoritos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{idUsuario}/eliminar/{idPokemon}")
    public ResponseEntity<Result> eliminar(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable int idUsuario,
            @Parameter(description = "ID del pokémon a eliminar", example = "25")
            @PathVariable int idPokemon) {
        return responder(favoritoService.eliminarFavorito(idUsuario, idPokemon));
    }
}
