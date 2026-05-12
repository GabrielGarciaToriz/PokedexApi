package com.digis.PokedexApi.dto;

import com.digis.PokedexApi.dto.pokemon.CriesDTO;
import com.digis.PokedexApi.dto.pokemon.MoveDTO;
import com.digis.PokedexApi.dto.pokemon.SpritesDTO;
import com.digis.PokedexApi.dto.pokemon.StatDTO;
import com.digis.PokedexApi.dto.pokemon.TypeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonApiResponseDTO {

    @JsonProperty("id")
    private int idPokemon;
    private String name;
    private int weight;
    private int height;
    @JsonProperty("base_experience")
    private int baseExperience;
    @JsonProperty("is_default")
    private boolean isDefault;
    private List<StatDTO> stats;
    private List<TypeDTO> types;
    private List<MoveDTO> moves;
    private SpritesDTO sprites;
    private CriesDTO cries;
}
