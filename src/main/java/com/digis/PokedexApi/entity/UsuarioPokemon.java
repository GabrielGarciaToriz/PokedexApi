package com.digis.PokedexApi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "usuariopokemon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPokemon {

    @Schema(description = "ID autogenerado del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuariopokemon")
    private Integer idUsuarioPokemon;
    @Schema(description = "Nombre(s) del usuario", example = "Ash")
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidopaterno")
    @Schema(description = "Apellido paterno", example = "Ketchum")
    private String apellidoPaterno;

    @Schema(description = "Apellido materno", example = "Oak")
    @Column(name = "apellidomaterno")
    private String apellidoMaterno;

    @Schema(description = "Fecha de nacimiento", example = "1997-04-01")
    @Column(name = "fechanacimiento")
    private Date fechaNacimiento;

    @Schema(description = "Nombre de usuario único", example = "ash_ketchum")
    @Column(name = "username")
    private String userName;

    @Schema(description = "Contraseña del usuario", example = "pikachu123")
    @Column(name = "password")
    private String password;

    @Schema(description = "Sexo: M o F", example = "M")
    @Column(name = "sexo")
    private char sexo;

    @Schema(description = "Rol asignado al usuario")
    @ManyToOne
    @JoinColumn(name = "idrol")
    public Rol rol;
}
