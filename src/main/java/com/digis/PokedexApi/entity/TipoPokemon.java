package com.digis.PokedexApi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipopokemon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa la categoría o tipo elemental de un Pokémon")
public class TipoPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipopokemon")
    @Schema(description = "Identificador único autogenerado para el tipo de Pokémon", example = "1")
    private Integer idTipoPokemon;

    @Column(name = "nombre")
    @Schema(description = "Nombre del tipo o elemento del Pokémon", example = "Fuego")
    private String nombre;

}
