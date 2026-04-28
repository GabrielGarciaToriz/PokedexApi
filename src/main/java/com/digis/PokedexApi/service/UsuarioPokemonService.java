package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.repository.UsuarioPokemonRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPokemonService {

    @Autowired
    private UsuarioPokemonRepository pokemonRepository;

    public Result getAll() {
        return envolverRespuesta(() -> pokemonRepository.getAllWithDetails());
    }

    public Result getAllByid(int idUsuarioPokemon) {
        return envolverRespuesta(() -> pokemonRepository.getAllByIdWithDetails(idUsuarioPokemon));
    }
    
    public Result getAllByUsername(String username){
        return envolverRespuesta(() -> pokemonRepository.getAllByUserNameWithDetails(username));
    }

    @Transactional
    public Result agregarUsuario(UsuarioPokemon usuario) {
        Result result = new Result();
        try {
            pokemonRepository.save(usuario);
            result.correct = true;
        } catch (Exception e) {
            manejoErrores(result, e);
        }
        return result;
    }

    public void manejoErrores(Result result, Exception ex) {
        result.correct = false;
        result.ex = ex;
        result.errorMessage = ex.getLocalizedMessage();
    }

    public Result envolverRespuesta(java.util.function.Supplier<List<UsuarioPokemon>> consulta) {
        Result result = new Result();
        try {
            result.objects = new ArrayList<>(consulta.get());
            result.correct = true;
        } catch (Exception e) {
            manejoErrores(result, e);
        }
        return result;
    }
}
