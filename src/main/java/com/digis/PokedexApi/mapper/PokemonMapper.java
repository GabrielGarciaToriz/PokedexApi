package com.digis.PokedexApi.mapper;

import com.digis.PokedexApi.dto.PokemonApiResponseDTO;
import com.digis.PokedexApi.dto.PokemonDTO;
import com.digis.PokedexApi.dto.pokemon.MoveDTO;
import com.digis.PokedexApi.dto.pokemon.StatDTO;
import com.digis.PokedexApi.dto.pokemon.TypeDTO;
import com.digis.PokedexApi.entity.Pokemon;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PokemonMapper {

    // ✅ PokemonApiResponseDTO (respuesta cruda PokeAPI) → PokemonDTO (caché)
    public PokemonDTO apiResponseToDTO(PokemonApiResponseDTO response) {
        if (response == null) {
            return null;
        }

        PokemonDTO dto = new PokemonDTO();
        dto.setIdPokemon(response.getIdPokemon());
        dto.setName(response.getName());
        dto.setWeight(String.valueOf(response.getWeight()));
        dto.setHeight(String.valueOf(response.getHeight()));
        dto.setBaseExperience(String.valueOf(response.getBaseExperience()));
        dto.setDefault(response.isDefault());
        dto.setTypes(mapearTipos(response.getTypes()));
        dto.setMoves(mapearMoves(response.getMoves()));
        dto.setStats(mapearStats(response.getStats()));
        dto.setSprites(response.getSprites());
        return dto;
    }

    // ✅ PokemonDTO (caché) → Pokemon (entidad BD para persistir)
    public Pokemon dtoToEntity(PokemonDTO dto) {
        if (dto == null) {
            return null;
        }

        Pokemon pokemon = new Pokemon();
        pokemon.setIdPokemon(dto.getIdPokemon());
        pokemon.setName(dto.getName());
        pokemon.setWeight(dto.getWeight());
        pokemon.setHeight(dto.getHeight());
        pokemon.setBaseExpirence(dto.getBaseExperience());
        pokemon.setDefault(dto.isDefault());
        pokemon.setTipoUno(
                dto.getTypes() != null && !dto.getTypes().isEmpty()
                ? dto.getTypes().get(0) : null
        );
        pokemon.setTipoDos(
                dto.getTypes() != null && dto.getTypes().size() > 1
                ? dto.getTypes().get(1) : null
        );
        pokemon.setSpriteFront(
                dto.getSprites() != null
                ? dto.getSprites().getFrontDefault() : null
        );
        return pokemon;
    }

    // ✅ Pokemon (entidad BD) → PokemonDTO (para devolver al frontend)
    public PokemonDTO entityToDTO(Pokemon pokemon) {
        if (pokemon == null) {
            return null;
        }

        PokemonDTO dto = new PokemonDTO();
        dto.setIdPokemon(pokemon.getIdPokemon());
        dto.setName(pokemon.getName());
        dto.setWeight(pokemon.getWeight());
        dto.setHeight(pokemon.getHeight());
        dto.setBaseExperience(pokemon.getBaseExpirence());
        dto.setDefault(pokemon.isDefault());
        // tipos: reconstruir lista desde tipo1 y tipo2
        List<String> tipos = new java.util.ArrayList<>();
        if (pokemon.getTipoUno() != null) {
            tipos.add(pokemon.getTipoUno());
        }
        if (pokemon.getTipoDos() != null) {
            tipos.add(pokemon.getTipoDos());
        }
        dto.setTypes(tipos.isEmpty() ? null : tipos);
        return dto;
    }

    // ── Métodos privados de mapeo ──────────────────────────────────────
    private List<String> mapearTipos(List<TypeDTO> types) {
        if (types == null) {
            return null;
        }
        return types.stream()
                .map(t -> t.getType().getName())
                .collect(Collectors.toList());
    }

    private Map<String, Integer> mapearStats(List<StatDTO> stats) {
        if (stats == null) {
            return null;
        }
        return stats.stream()
                .collect(Collectors.toMap(
                        s -> s.getStat().getName(),
                        StatDTO::getBaseStat
                ));
    }

    private List<String> mapearMoves(List<MoveDTO> moves) {
        if (moves == null) {
            return null;
        }
        return moves.stream()
                .map(m -> m.getMove().getName())
                .collect(Collectors.toList());
    }
}
