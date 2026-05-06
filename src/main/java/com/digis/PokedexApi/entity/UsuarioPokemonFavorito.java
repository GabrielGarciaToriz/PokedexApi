package com.digis.PokedexApi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;
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
@Table(name = "usuario_pokemon_favorito", uniqueConstraints = {
    @UniqueConstraint(name = "UK_USUARIO_POKEMON_FAVORITO", columnNames = {"idusuariopokemon", "idpokemon"})
})
@Schema(description = "Relación entre un usuario y su pokémon favorito")
public class UsuarioPokemonFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuariopokemonfavorito")
    @Schema(description = "ID autogenerado del favorito", example = "10")
    private Integer idUsuarioPokemonFavorito;
    @ManyToOne
    @JoinColumn(name = "idusuariopokemon")
    @Schema(description = "Usuario propietario del favorito")
    private UsuarioPokemon usuarioPokemon;
    @ManyToOne
    @JoinColumn(name = "idpokemon")
    @Schema(description = "Pokémon guardado como favorito")
    private Pokemon pokemon;
    @Schema(description = "Fecha en que se agregó el favorito", example = "2025-05-01")
    @Column(name = "fechaagregado")
    @Temporal(TemporalType.DATE)
    private Date fechaAgregado;
}
