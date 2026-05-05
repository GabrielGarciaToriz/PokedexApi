package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.repository.UsuarioPokemonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPokemonService extends BaseService {

    @Autowired
    private UsuarioPokemonRepository usuarioRepository;

    public Result getAll() {
        return ejecutarLista(() -> usuarioRepository.getAllWithDetails());
    }

    public Result getAllByid(int idUsuarioPokemon) {
        return ejecutarLista(() -> usuarioRepository.getAllByIdWithDetails(idUsuarioPokemon));
    }

    public Result getAllByUsername(String username) {
        return ejecutarLista(() -> usuarioRepository.getAllByUserNameWithDetails(username));
    }

    public Result buscarUsuario(String nombre, String apellidoPaterno, String apellidoMaterno) {
        return ejecutarLista(() -> usuarioRepository.buscarPorFiltros(nombre, apellidoPaterno, apellidoMaterno));
    }

    @Transactional
    public Result agregarUsuario(UsuarioPokemon usuario) {
        return ejecutarVoid(() -> usuarioRepository.save(usuario));
    }

    @Transactional
    public Result eliminarUsuario(int idUsuario) {
        return ejecutarVoid(() -> usuarioRepository.deleteById(idUsuario));
    }

}
