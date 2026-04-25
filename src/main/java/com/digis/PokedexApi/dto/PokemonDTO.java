package com.digis.PokedexApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonDTO {

    private int idPokemon;
    private String name;
    private String weight;
    private String height;
    @JsonProperty("base_experience")
    private String baseExperience;
    @JsonProperty("is_default")
    private boolean isDefault;

}
