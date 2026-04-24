package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.UsuarioPokemon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioPokemonRepository extends JpaRepository<UsuarioPokemon, Integer> {

    @Query("""
           SELECT DISTINCT u FROM UsuarioPokemon u
           LEFT JOIN FETCH u.rol
           ORDER BY u.idUsuarioPokemon ASC
           """)
    List<UsuarioPokemon> getAllWithDetails();

    @Query("""
           SELECT DISTINCT u FROM UsuarioPokemon u
           LEFT JOIN FETCH u.rol
           WHERE u.idUsuarioPokemon = :idUsuarioPokemon
           ORDER BY u.idUsuarioPokemon ASC
           """
    )
    List<UsuarioPokemon> getAllByIdWithDetails(@Param("idUsuarioPokemon") int idUsuarioPokemon);
    
    @Query(
    """
    SELECT DISTINCT u FROM UsuarioPokemon u
    LEFT JOIN FETCH u.rol
    WHERE u.userName = :userName
    ORDER BY u.idUsuarioPokemon ASC
    """
    )
    List<UsuarioPokemon> getAllByUserNameWithDetails(@Param("userName")String username);
}
