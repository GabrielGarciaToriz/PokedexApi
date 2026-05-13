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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPASSWORDTOKEN")
    private Long idPasswordToken;
    @Column(nullable = false, unique = true)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuarioPokemon", nullable = false)
    private UsuarioPokemon usuario;
    @Column(nullable = false, name = "expiration")
    private LocalDateTime expiration;
    @Column(nullable = false, name = "usado")
    private boolean usado;

    public boolean estaExpirado() {
        return LocalDateTime.now().isAfter(this.expiration);
    }

    public boolean esValido() {
        return !usado && !estaExpirado();
    }

}
