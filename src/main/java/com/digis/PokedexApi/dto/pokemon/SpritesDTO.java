package com.digis.PokedexApi.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpritesDTO {

    @JsonProperty("front_default")
    private String frontDefault;
    @JsonProperty("back_default")
    private String backDefault;
    @JsonProperty("front_shiny")
    private String frontShiny;
    @JsonProperty("back_shiny")
    private String backShiny;
}
