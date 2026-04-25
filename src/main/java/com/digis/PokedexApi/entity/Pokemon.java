package com.digis.PokedexApi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {

    private int idPokemon;
    private String name;
    private String weight;
    private String height;
    private String baseExpirence;
    private boolean isDefault;
    
}
