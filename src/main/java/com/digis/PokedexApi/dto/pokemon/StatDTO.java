package com.digis.PokedexApi.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatDTO {
    @JsonProperty("base_stat")
    private int baseStat;
    private StatInfoDTO stat;
}
