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
public class UsuarioPokemonService extends BaseService {

    @Autowired
    private UsuarioPokemonRepository usuarioRepository;

    public Result getAll() {
        return envolverRespuesta(() -> usuarioRepository.getAllWithDetails());
    }

    public Result getAllByid(int idUsuarioPokemon) {
        return envolverRespuesta(() -> usuarioRepository.getAllByIdWithDetails(idUsuarioPokemon));
    }

    public Result getAllByUsername(String username) {
        return envolverRespuesta(() -> usuarioRepository.getAllByUserNameWithDetails(username));
    }

    public Result buscarUsuario(String nombre, String apellidoPaterno, String apellidoMaterno) {
        return ejecutarLista(() -> usuarioRepository.buscarPorFiltros(nombre, apellidoPaterno, apellidoMaterno));
    }

    @Transactional
    public Result agregarUsuario(UsuarioPokemon usuario) {
        Result result = new Result();
        try {
            usuarioRepository.save(usuario);
            result.correct = true;
        } catch (Exception e) {
            manejoErrores(result, e);
        }
        return result;
    }

    @Transactional
    public Result eliminarUsuario(int idUsuario) {
        Result result = new Result();
        try {
            usuarioRepository.deleteById(idUsuario);
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
