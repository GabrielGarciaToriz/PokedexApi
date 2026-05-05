package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.repository.TipoPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogoService extends BaseService {

    @Autowired
    private TipoPokemonRepository tipoPokemonRepository;

    public Result getAllTipos() {
        return ejecutarLista(() -> tipoPokemonRepository.findAll());
    }
}
