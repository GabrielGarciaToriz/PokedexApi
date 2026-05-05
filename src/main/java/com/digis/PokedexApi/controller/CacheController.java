package com.digis.PokedexApi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cache")
@Tag(name = "Caché", description = "Administración del caché de la aplicación")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @Operation(summary = "Limpiar todo el caché",
               description = """
                   Limpia todos los cachés gestionados por Spring CacheManager.
                   Afecta: `pokemon-id`, `pokemon-name`, `pokemon-todos`, `pokemon-paginado`.
                   """)
    @ApiResponse(responseCode = "200", description = "Caché limpiado exitosamente")
    @DeleteMapping("/limpiar")
    public ResponseEntity<String> limpiarCache() {
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
        return ResponseEntity.ok("Cache limpiado correctamente");
    }
}