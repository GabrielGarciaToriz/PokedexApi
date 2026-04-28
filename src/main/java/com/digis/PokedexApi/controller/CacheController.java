package com.digis.PokedexApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @DeleteMapping("/limpiar")
    public ResponseEntity<String> limpiarCache() {
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
        return ResponseEntity.ok("Cache listo correcctameente");
    }
}
