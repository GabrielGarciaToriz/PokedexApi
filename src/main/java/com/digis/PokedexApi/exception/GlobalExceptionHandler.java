package com.digis.PokedexApi.exception;

import com.digis.PokedexApi.dto.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Error UNIQUE violado, un usuario no puede repetir un pokemon como su favorito
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> violationUniqueConstraintPokemon(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Este pokémon ya esta en tu lista de favoritos", HttpStatus.CONFLICT);
    }

    // Pokemon no encontrado en la PokeAPI (404 externo)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Result> handleNotFound(HttpClientErrorException.NotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Result.error(ErrorCode.NOT_FOUND,
                        "El recurso solicitado no fue encontrado"));
    }

    // PokeAPI no responde (timeout, red caída)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Result> handleApiDown(ResourceAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Result.error(ErrorCode.EXTERNAL_API,
                        "No se pudo conectar con la PokeAPI, intenta más tarde"));
    }

    // Cualquier error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(ErrorCode.INTERNAL_ERROR,
                        "Ocurrió un error inesperado en el servidor"));
    }
}
