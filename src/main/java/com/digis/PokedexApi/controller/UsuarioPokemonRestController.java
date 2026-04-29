package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.service.UsuarioPokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/usuario")
public class UsuarioPokemonRestController {

    @Autowired
    private UsuarioPokemonService usuarioPokemonService;

    @GetMapping()
    public ResponseEntity<Result> getAll() {
        Result result = usuarioPokemonService.getAll();
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{idUsuarioPokemon}")
    public ResponseEntity<Result> getAllById(@PathVariable int idUsuarioPokemon) {
        Result result = usuarioPokemonService.getAllByid(idUsuarioPokemon);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Result> getAllByUsername(@PathVariable String username) {
        Result result = usuarioPokemonService.getAllByUsername(username);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping()
    public ResponseEntity<Result> agregarUsaurio(@RequestBody UsuarioPokemon usuario) {
        Result result = usuarioPokemonService.agregarUsuario(usuario);
        return new ResponseEntity<>(result, result.correct ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Result> eliminarUsuario(@PathVariable("id") int idUsuarioPokemon) {
        Result result = usuarioPokemonService.eliminarUsuario(idUsuarioPokemon);
        if (result.correct) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
        } else {
            HttpStatus statusError = result.errorMessage
                    .contains("no encontrado") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(statusError).body(result);
        }

    }
}
