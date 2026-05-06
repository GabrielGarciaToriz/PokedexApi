package com.digis.PokedexApi.dto.Login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    private String correo;
    private String password;
}
