    package com.digis.PokedexApi.dto.password;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResetPasswordRequest {

    private String token;
    private String nuevaPassword;

}
