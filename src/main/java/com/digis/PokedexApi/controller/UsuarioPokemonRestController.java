package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.service.UsuarioPokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario")
@Tag(name = "Usuarios", description = "CRUD de usuarios Pokémon")
public class UsuarioPokemonRestController extends BaseController {

    @Autowired
    private UsuarioPokemonService usuarioPokemonService;

    @Operation(summary = "Obtener todos los usuarios",
            description = "Retorna la lista completa de usuarios con sus detalles y rol asignado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ResponseEntity<Result> getAll() {
        return responder(usuarioPokemonService.getAll());
    }

    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{idUsuarioPokemon}")
    public ResponseEntity<Result> getAllById(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable int idUsuarioPokemon) {
        return responder(usuarioPokemonService.getAllByid(idUsuarioPokemon));
    }

    @Operation(summary = "Obtener usuario por username")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<Result> getAllByUsername(
            @Parameter(description = "Nombre de usuario (username)", example = "ash_ketchum")
            @PathVariable String username) {
        return responder(usuarioPokemonService.getAllByUsername(username));
    }

    @Operation(summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario con sus datos personales y rol.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "422", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/agregar")
    public ResponseEntity<Result> agregarUsuario(
            @RequestBody(description = "Datos del nuevo usuario a registrar", required = true)
            @org.springframework.web.bind.annotation.RequestBody UsuarioPokemon usuario) {
        return responderCreado(usuarioPokemonService.agregarUsuario(usuario));
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario por su ID. Retorna `204 No Content` si fue exitoso.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Result> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable("id") int idUsuarioPokemon) {
        return responderEliminado(usuarioPokemonService.eliminarUsuario(idUsuarioPokemon));
    }
}
