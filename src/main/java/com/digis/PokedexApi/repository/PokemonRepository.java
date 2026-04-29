package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {
}
