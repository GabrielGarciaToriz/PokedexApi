package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.PokemonDTO;
import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.entity.Pokemon;
import com.digis.PokedexApi.entity.UsuarioPokemon;
import com.digis.PokedexApi.entity.UsuarioPokemonFavorito;
import com.digis.PokedexApi.exception.ErrorCode;
import com.digis.PokedexApi.mapper.PokemonMapper;
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
    @Autowired
    private PokeApiService pokeApiService;
    @Autowired
    private PokemonMapper mapper;

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
    public Result agregarFavoritoDesdeCache(int idUsuario, Pokemon pokemon) {
        try {
            if (pokemonFavoritoRepository.existsByUsuarioPokemon_IdUsuarioPokemonAndPokemon_IdPokemon(idUsuario, idUsuario)) {
                Result.error(ErrorCode.DUPLICATE, "Este pokemon ya es tu favorito");
            }

            PokemonDTO pokemonDTO = pokeApiService.getFromCacheById(idUsuario);

            if (pokemonDTO == null) {
                return Result.error(ErrorCode.NOT_FOUND, "Pokemon no encontrado");
            }
            
        } catch (Exception e) {
        }
        return null;
    }

    @Transactional
    public Result eliminarFavorito(int idUsuario, int idPokemon) {
        return ejecutar(() -> {
            UsuarioPokemonFavorito favorito = pokemonFavoritoRepository
                    .getFavoritosByUsuario(idUsuario).stream()
                    .filter(f -> f.getPokemon().getIdPokemon() == idPokemon)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                    "El pokémon no está en tu lista de favoritos"));
            pokemonFavoritoRepository.delete(favorito);
            return true;
        });
    }
}
