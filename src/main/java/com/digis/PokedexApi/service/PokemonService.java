package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.Pokemon;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.entity.UsuarioPokemonFavorito;
import com.digis.PokedexApi.repository.PokemonRepository;
import com.digis.PokedexApi.repository.UsuarioPokemonFavoritoRepository;
import jakarta.transaction.Transactional;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonService extends BaseService {

    @Autowired
    private UsuarioPokemonFavoritoRepository pokemonFavoritoRepository;
    @Autowired
    private PokemonRepository pokemonRepository;

    public Result getFavoritoById(int idUsuario) {
        return ejecutarLista(() -> pokemonFavoritoRepository.getFavoritosByUsuario(idUsuario));
    }

    public Result isFav(int idUsuario, int idPokemon) {
        return ejecutar(() -> pokemonFavoritoRepository.existsByUsuarioPokemon_IdUsuarioPokemonAndPokemon_IdPokemon(idUsuario, idPokemon));
    }

    public Result buscarPokemon(String nombre, String tipo) {
        return ejecutarLista(
                () -> pokemonRepository.buscarPorFiltros(nombre, tipo)
        );
    }
    

    @Transactional
    public Result agregarFavorito(int idUsuario, Pokemon pokemon) {
        Result result = new Result();
        try {
            if (pokemonFavoritoRepository.existsByUsuarioPokemon_IdUsuarioPokemonAndPokemon_IdPokemon(idUsuario, pokemon.getIdPokemon())) {
                result.correct = false;
                result.errorMessage = "Este pokemon ya esta en tus favoritos";
                return result;
            }
            if (!pokemonRepository.existsById(pokemon.getIdPokemon())) {
                pokemonRepository.save(pokemon);
            }

            UsuarioPokemonFavorito favorito = new UsuarioPokemonFavorito();
            UsuarioPokemon usuario = new UsuarioPokemon();
            usuario.setIdUsuarioPokemon(idUsuario);
            favorito.setUsuarioPokemon(usuario);
            favorito.setPokemon(pokemon);
            favorito.setFechaAgregado(new Date());
            pokemonFavoritoRepository.save(favorito);
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }

    @Transactional
    public Result eliminarFavorito(int idUsuario, int idPokemon) {
        Result result = new Result();
        try {
            pokemonFavoritoRepository.getFavoritosByUsuario(idUsuario).stream()
                    .filter(f -> f.getPokemon().getIdPokemon() == idPokemon)
                    .findFirst()
                    .ifPresentOrElse(pokemonFavoritoRepository::delete,
                            () -> {
                                throw new RuntimeException("No encontrado");
                            }
                    );
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
        }
        return result;
    }
}
