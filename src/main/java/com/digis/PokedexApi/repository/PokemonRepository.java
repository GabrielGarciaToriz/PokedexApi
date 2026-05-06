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
            WHERE (:id IS NULL OR p.idPokemon = :id)
            AND (:nombre IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :nombre, '%')))
            AND (:tipo1 IS NULL OR LOWER(p.tipoUno) = LOWER(:tipo1) OR LOWER(p.tipoDos) = LOWER(:tipo1))
            AND (:tipo2 IS NULL OR LOWER(p.tipoUno) = LOWER(:tipo2) OR LOWER(p.tipoDos) = LOWER(:tipo2))
            ORDER BY p.idPokemon ASC
    """)
    List<Pokemon> buscarPorFiltros(
            @Param("id") Integer id,
            @Param("nombre") String nombre,
            @Param("tipo1") String tipo1,
            @Param("tipo2") String tipo2);

}
