package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/password")
@Tag(name = "Password Reset", description = "Flujo de recuperación de contraseña por correo")
public class PasswordResetController extends BaseController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Operation(summary = "Solicitar recuperación",
               description = "Recibe el email, valida que exista y esté activo, y envía el correo con el token.")
    @PostMapping("/forgot")
    public ResponseEntity<Result> forgot(@RequestParam String correo) {
        return responder(passwordResetService.solicitarRecuperacion(correo));
    }

    @Operation(summary = "Validar token",
               description = "El frontend llama esto al cargar /reset-password para saber si el token sigue siendo válido.")
    @GetMapping("/validate")
    public ResponseEntity<Result> validate(@RequestParam String token) {
        return responder(passwordResetService.validarToken(token));
    }

    @Operation(summary = "Cambiar contraseña",
               description = "Recibe el token y la nueva contraseña. Invalida el token tras el cambio.")
    @PostMapping("/reset")
    public ResponseEntity<Result> reset(
            @RequestParam String token,
            @RequestParam String nuevaPassword) {
        return responder(passwordResetService.cambiarPassword(token, nuevaPassword));
    }
}