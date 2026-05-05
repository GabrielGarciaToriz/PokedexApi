package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.service.UsuarioPokemonService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsuarioPokemonRestController extends BaseController {

    @Autowired
    private UsuarioPokemonService usuarioPokemonService;

    @GetMapping()
    public ResponseEntity<Result> getAll() {
        return responder(usuarioPokemonService.getAll());
    }

    @GetMapping("/{idUsuarioPokemon}")
    public ResponseEntity<Result> getAllById(@PathVariable int idUsuarioPokemon) {
        return responder(usuarioPokemonService.getAllByid(idUsuarioPokemon));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Result> getAllByUsername(@PathVariable String username) {
        return responder(usuarioPokemonService.getAllByUsername(username));
    }

    @PostMapping("/agregar")
    public ResponseEntity<Result> agregarUsuario(@RequestBody UsuarioPokemon usuario) {
        return responderCreado(usuarioPokemonService.agregarUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> eliminarUsuario(@PathVariable("id") int idUsuarioPokemon) {
        return responderEliminado(usuarioPokemonService.eliminarUsuario(idUsuarioPokemon));

    }
}
