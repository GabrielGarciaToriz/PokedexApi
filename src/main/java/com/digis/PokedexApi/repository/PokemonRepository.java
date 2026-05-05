package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.Pokemon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

    @Query(
            """
            SELECT p FROM Pokemon p
            WHERE (:nombre IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :nombre, '%')))
            AND (:tipo IS NULL OR LOWER(p.tipoUno) = LOWER(:tipo)
            OR LOWER(p.tipoDos) = LOWER(:tipo))
            ORDER BY p.idPokemon ASC
    """)
    List<Pokemon> buscarPorFiltros(
            @Param("nombre") String nombre,
            @Param("tipo") String tipo);

  
}
