package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.UsuarioPokemonFavorito;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioPokemonFavoritoRepository extends JpaRepository<UsuarioPokemonFavorito, Integer> {

    @Query(
            """
            SELECT f FROM UsuarioPokemonFavorito f
            LEFT JOIN FETCH f.pokemon
            LEFT JOIN FETCH f.usuarioPokemon
            WHERE f.usuarioPokemon.idUsuarioPokemon = :idUsuario
            ORDER BY f.fechaAgregado DESC
    """
    )
    List<UsuarioPokemonFavorito> getFavoritosByUsuario(@Param("idUsuario") int idUsuario);

    boolean existsByUsuarioPokemon_IdUsuarioPokemonAndPokemon_IdPokemon(int idUsuario, int idPokemon);
}
