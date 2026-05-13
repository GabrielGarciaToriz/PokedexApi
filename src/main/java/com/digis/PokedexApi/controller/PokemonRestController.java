package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/favorito")
@Tag(name = "Favoritos", description = "Gestión de pokémon favoritos por usuario")
public class PokemonRestController extends BaseController {

    @Autowired
    private PokemonService favoritoService;

    @Operation(summary = "Obtener favoritos de un usuario")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Result> getFavoritos(@PathVariable("idUsuario") int idUsuario) {
        return responder(favoritoService.getFavoritoById(idUsuario));
    }

    @Operation(summary = "Verificar si un pokémon es favorito")
    @GetMapping("/{idUsuario}/existe/{idPokemon}")
    public ResponseEntity<Result> esFavorito(
            @PathVariable int idUsuario, @PathVariable int idPokemon) {
        return responder(favoritoService.isFav(idUsuario, idPokemon));
    }

    @Operation(summary = "Agregar pokémon a favoritos", description = "Busca en el caché en memoria y lo persiste.")
    @PostMapping("/{idUsuario}/pokemon/{idPokemon}")
    public ResponseEntity<Result> agregarFavorito(
            @PathVariable("idUsuario") int idUsuario, @PathVariable int idPokemon) {
        return responder(favoritoService.agregarFavoritoDesdeCache(idUsuario, idPokemon));
    }

    @Operation(summary = "Eliminar pokémon de favoritos")
    @DeleteMapping("/{idUsuario}/eliminar/{idPokemon}")
    public ResponseEntity<Result> eliminar(
            @PathVariable int idUsuario, @PathVariable int idPokemon) {
        return responder(favoritoService.eliminarFavorito(idUsuario, idPokemon));
    }
}