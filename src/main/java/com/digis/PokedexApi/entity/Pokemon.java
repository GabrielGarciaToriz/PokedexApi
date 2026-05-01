package com.digis.PokedexApi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Pokemon")
public class Pokemon {

    @Id
    @Column(name = "idpokemon")
    private Integer idPokemon;
    @Column(name = "nombre")
    private String name;
    @Column(name = "peso")
    private String weight;
    @Column(name = "altura")
    private String height;
    @Column(name = "tipo1")
    private String tipoUno;
    @Column(name = "tipo2")
    private String tipoDos;
    @Column(name = "experiencia_base")
    private String baseExpirence;
    @Column(name = "sprite_front")
    private String spriteFront;
    @Column(name = "isdefault")
    private boolean isDefault;
}
