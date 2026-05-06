package com.digis.PokedexApi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token_activacion")
public class TokenActivacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtoken")
    private Integer idToken;
    @Column(name = "token", unique = true, nullable = false, length = 64)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuariopokemon", nullable = false)
    private UsuarioPokemon usuarioPokemon;
    @Column(name = "fechaexpiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
    @Column(name = "usado")
    private boolean usado = false;

    public TokenActivacion(String token, UsuarioPokemon usuarioPokemon, LocalDateTime fechaExpiracion) {
        this.token = token;
        this.usuarioPokemon = usuarioPokemon;
        this.fechaExpiracion = fechaExpiracion;
        this.usado = false;
    }

}
