package com.digis.PokedexApi.repository;

import com.digis.PokedexApi.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonApiRepository extends JpaRepository<Pokemon, Integer>{
 
    
}
