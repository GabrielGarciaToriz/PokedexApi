package com.digis.PokedexApi.dto.pokemon;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokeListResponseDTO {

    private int count;
    private String next;
    private String previous;
    private List<PokeListItemDTO> results;
}
