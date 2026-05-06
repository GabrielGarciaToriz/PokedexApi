package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.TokenActivacion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenActivacionRepository extends JpaRepository<TokenActivacion, Integer> {

    Optional<TokenActivacion> findByToken(String token);

    @Modifying
    @Query("""
           DELETE FROM TokenActivacion t WHERE t.usuarioPokemon.idUsuarioPokemon = :idUsuarioPokemon
           """)
    void deleteByUsuarioId(@Param("idUsuarioPokemon") Integer idUsuarioPokemon);
}
