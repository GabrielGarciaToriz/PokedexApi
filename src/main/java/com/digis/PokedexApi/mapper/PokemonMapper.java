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

    public PokemonDTO toEntity(PokemonApiResponseDTO pokemonRespuesta) {
        if (pokemonRespuesta == null) {
            return null;
        }
        PokemonDTO pokemon = new PokemonDTO();
        pokemon.setIdPokemon(pokemonRespuesta.getIdPokemon());
        pokemon.setName(pokemonRespuesta.getName());
        pokemon.setWeight(String.valueOf(pokemonRespuesta.getWeight()));
        pokemon.setHeight(String.valueOf(pokemonRespuesta.getHeight()));
        pokemon.setBase_expirence(String.valueOf(pokemonRespuesta.getBaseExperience()));
        pokemon.set_default(pokemonRespuesta.isDefault());
        List<String> types = pokemonRespuesta.getTypes().stream().map(t -> t.getType().getName()).collect(Collectors.toList());
        pokemon.setTypes(types);
        List<String> moves = pokemonRespuesta.getMoves().stream().map(m -> m.getMove().getName()).collect(Collectors.toList());
        pokemon.setMoves(moves);
        Map<String, Integer> stats = pokemonRespuesta.getStats().stream().collect(Collectors.toMap(s -> s.getStat().getName(), StatDTO::getBaseStat));
        pokemon.setStats(stats);
        pokemon.setSprites(pokemonRespuesta.getSprites());
        return pokemon;

    }

    public Pokemon toFavorito(PokemonDTO pokemonDTO) {
        Pokemon pokemon = new Pokemon();
        pokemon.setIdPokemon(pokemonDTO.getIdPokemon());
        pokemon.setName(pokemonDTO.getName());
        pokemon.setWeight(pokemonDTO.getWeight());
        pokemon.setHeight(pokemonDTO.getHeight());
        pokemon.setTipo_uno(pokemonDTO.getTypes().size() > 0 ? pokemonDTO.getTypes().get(0) : null);
        pokemon.setTipo_dos(pokemonDTO.getTypes().size() > 1 ? pokemonDTO.getTypes().get(1) : null);
        pokemon.setSprite_front(pokemonDTO.getSprites() != null
                ? pokemonDTO.getSprites().getFrontDefault() : null);
        return pokemon;
    }

    private List<String> mapearTipos(List<TypeDTO> types) {
        return types.stream()
                .map(t -> t.getType().getName())
                .collect(Collectors.toList());
    }

    private Map<String, Integer> mapearStats(List<StatDTO> stats) {
        return stats.stream()
                .collect(Collectors.toMap(
                        s -> s.getStat().getName(),
                        StatDTO::getBaseStat
                ));
    }

    private List<String> mapearMoves(List<MoveDTO> moves) {
        return moves.stream()
                .map(m -> m.getMove().getName())
                .collect(Collectors.toList());
    }
}
