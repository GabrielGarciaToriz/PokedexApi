package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected ResponseEntity<Result> responder(Result result) {
        return new ResponseEntity<>(result,
                result.correct ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Result> responder(Result result,
            HttpStatus errorStatus) {
        return new ResponseEntity<>(result,
                result.correct ? HttpStatus.OK : errorStatus);
    }
}
