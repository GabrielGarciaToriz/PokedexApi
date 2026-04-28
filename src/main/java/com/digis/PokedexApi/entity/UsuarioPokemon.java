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

@Entity
@Table(name = "usuariopokemon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuariopokemon")
    private Integer idUsuarioPokemon;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellidopaterno")
    private String apellidoPaterno;
    @Column(name = "apellidomaterno")
    private String apellidoMaterno;
    @Column(name = "fechanacimiento")
    private Date fechaNacimiento;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "sexo")
    private char sexo;
    @ManyToOne
    @JoinColumn(name = "idrol")
    public Rol rol;
}
