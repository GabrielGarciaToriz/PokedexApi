package com.digis.PokedexApi.dto.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String correo;
    private String nombre;
    private String rol;
}
