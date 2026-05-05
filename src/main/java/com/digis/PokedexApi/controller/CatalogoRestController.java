package com.digis.PokedexApi.controller;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/catalogo")
public class CatalogoRestController extends BaseController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping()
    public ResponseEntity<Result> getAll() {
        return responder(catalogoService.getAllTipos());
    }
}
