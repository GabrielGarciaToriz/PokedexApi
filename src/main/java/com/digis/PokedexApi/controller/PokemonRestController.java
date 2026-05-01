package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.Pokemon;
import com.digis.PokedexApi.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/favorito")
public class PokemonRestController extends BaseController {

    @Autowired
    private PokemonService favoritoService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Result> getFavoritos(@PathVariable("idUsuario") int idUsuario) {
        return responder(favoritoService.getFavoritoById(idUsuario));

    }

    @GetMapping("/{idUsuario}/existe/{idPokemon}")
    public ResponseEntity<Result> esFavorito(
            @PathVariable int idUsuario,
            @PathVariable int idPokemon) {
        return responder(favoritoService.isFav(idUsuario, idPokemon));
    }
    

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Result> agregarFavorito(@PathVariable("idUsuario") int idUsuario, @RequestBody Pokemon pokemon) {
        return responder(favoritoService.agregarFavoritoDesdeCache(idUsuario, pokemon));
    }

    @DeleteMapping("/{idUsuario}/eliminar/{idPokemon}")
    public ResponseEntity<Result> eliminar(
            @PathVariable int idUsuario, @PathVariable int idPokemon) {
        return responder(favoritoService.eliminarFavorito(idUsuario, idPokemon));
    }
}
