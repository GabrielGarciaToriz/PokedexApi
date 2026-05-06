package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.repository.RolRepository;
import com.digis.PokedexApi.repository.TipoPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogoService extends BaseService {

    @Autowired
    private TipoPokemonRepository tipoPokemonRepository;
    @Autowired
    private RolRepository rolRepository;

    public Result getAllTipos() {
        return ejecutarLista(() -> tipoPokemonRepository.findAll());
    }

    public Result getAllRol() {
        return ejecutarLista(() -> rolRepository.findAll());
    }
}
