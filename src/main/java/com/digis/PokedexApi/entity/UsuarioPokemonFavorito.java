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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario_pokemon_favorito", uniqueConstraints = {
    @UniqueConstraint(name = "UK_USUARIO_POKEMON_FAVORITO", columnNames = {"idusuariopokemon", "idpokemon"})
})
public class UsuarioPokemonFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuariopokemonfavorito")
    private Integer idUsuarioPokemonFavorito;
    @ManyToOne
    @JoinColumn(name = "idusuariopokemon")
    private UsuarioPokemon usuarioPokemon;
    @ManyToOne
    @JoinColumn(name = "idpokemon")
    private Pokemon pokemon;
    @Column(name = "fechaagregado")
    @Temporal(TemporalType.DATE)
    private Date fechaAgregado;
}
