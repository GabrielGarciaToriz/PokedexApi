package com.digis.PokedexApi.dto.password;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ForgotPasswordRequest {

    private String correo;
}
