package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected ResponseEntity<Result> responder(Result result) {
        return new ResponseEntity<>(result, result.correct ? HttpStatus.OK : resolverStatus(result));
    }

    protected ResponseEntity<Result> responderCreado(Result result) {
        return new ResponseEntity<>(result, result.correct ? HttpStatus.CREATED : resolverStatus(result));
    }

    protected ResponseEntity<Result> responderEliminado(Result result) {
        if (result.correct) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(result, resolverStatus(result));
    }

    private HttpStatus resolverStatus(Result result) {
        if (result.errorCode == null) {
            return HttpStatus.BAD_REQUEST;
        }
        return switch (result.errorCode) {
            case ErrorCode.NOT_FOUND ->
                HttpStatus.NOT_FOUND;           // 404
            case ErrorCode.DUPLICATE ->
                HttpStatus.CONFLICT;            // 409
            case ErrorCode.INVALID_INPUT ->
                HttpStatus.UNPROCESSABLE_ENTITY;// 422
            case ErrorCode.INTERNAL_ERROR ->
                HttpStatus.INTERNAL_SERVER_ERROR;// 500
            default ->
                HttpStatus.BAD_REQUEST;         // 400
        };
    }
}
