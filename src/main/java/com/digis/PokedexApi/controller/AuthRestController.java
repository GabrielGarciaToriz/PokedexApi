package com.digis.PokedexApi.controller;
import com.digis.PokedexApi.dto.Login.LoginRequestDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.UsuarioPokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthRestController extends BaseController {

    @Autowired
    private UsuarioPokemonService usuarioPokemonService;

    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody LoginRequestDTO request) {
        return responder(usuarioPokemonService.login(request));
    }

    @GetMapping("/activar")
    public ResponseEntity<Result> activarCuenta(@RequestParam String token) {
        return responder(usuarioPokemonService.activarCuenta(token));
    }

    // Por si el usuario no recibió el correo
    @PostMapping("/reenviar-activacion")
    public ResponseEntity<Result> reenviarActivacion(@RequestParam String email) {
        return responder(usuarioPokemonService.reenviarActivacion(email));
    }
}
