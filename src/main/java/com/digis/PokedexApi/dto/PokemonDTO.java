package com.digis.PokedexApi.dto;

import com.digis.PokedexApi.dto.pokemon.SpritesDTO;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PokemonDTO {

    private int idPokemon;
    private String name;
    private String weight;
    private String height;
    private String base_expirence;
    private boolean is_default;

    private List<String> types;
    private List<String> moves;
    private Map<String, Integer> stats;
    private SpritesDTO sprites;

}
