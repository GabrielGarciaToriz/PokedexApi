package com.digis.PokedexApi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Pokemon")
@Schema(description = "Entidad Pokémon persistida en base de datos")
public class Pokemon {

    @Schema(description = "ID del pokémon (corresponde al ID de la PokéAPI)", example = "25")
    @Id
    @Column(name = "idpokemon")
    private Integer idPokemon;
    @Schema(description = "Nombre del pokémon", example = "pikachu")
    @Column(name = "nombre")
    private String name;
    @Schema(description = "Peso en kg", example = "60")
    @Column(name = "peso")
    private String weight;
    @Schema(description = "Altura en m", example = "4")
    @Column(name = "altura")
    private String height;
    @Schema(description = "Tipo primario", example = "electric")
    @Column(name = "tipo1")
    private String tipoUno;
    @Schema(description = "Tipo secundario (puede ser null)", example = "null", nullable = true)
    @Column(name = "tipo2")
    private String tipoDos;
    @Schema(description = "Experiencia base", example = "112")
    @Column(name = "experiencia_base")
    private String baseExpirence;
    @Schema(description = "URL del sprite frontal", example = "https://raw.githubusercontent.com/.../pikachu.png")
    @Column(name = "sprite_front")
    private String spriteFront;
    @Schema(description = "Indica si es la forma por defecto del pokémon", example = "true")
    @Column(name = "isdefault")
    private boolean isDefault;
}
