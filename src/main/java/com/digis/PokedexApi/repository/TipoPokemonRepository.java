package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.TipoPokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPokemonRepository extends JpaRepository<TipoPokemon, Integer> {

}
