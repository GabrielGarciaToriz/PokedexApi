package com.digis.PokedexApi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private int idPokemon;
    private String nombre;
    private double peso;
    private double altura;
    private String tipo_uno;
    private String tipo_dos;
    private String habilidad_uno;
    private String habilidad_dos;
    private String movimiento_uno;
    private String movimiento_dos;
    private String movimiento_tres;
    
}
