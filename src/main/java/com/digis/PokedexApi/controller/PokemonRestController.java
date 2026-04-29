package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/favorito")
public class PokemonRestController {

    @Autowired
    private PokemonService favoritoRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Result> getFavoritos(@PathVariable("idUsuario") int idUsuario) {
        Result result = favoritoRepository.getFavoritoById(idUsuario);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/{idUsuario}/existe/{idPokemon}")
    public ResponseEntity<Result> esFavorito(
            @PathVariable int idUsuario,
            @PathVariable int idPokemon) {
        Result result = favoritoRepository.isFav(idUsuario, idPokemon);
        if (!result.correct) {
            result.errorMessage = "Este poquemon no es favortio aun";
        }
        return new ResponseEntity<>(result,
                result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
