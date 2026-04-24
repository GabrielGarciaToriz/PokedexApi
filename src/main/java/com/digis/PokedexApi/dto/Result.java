package com.digis.PokedexApi.dto;


import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Result {

    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public Object object;
    public List<?> objects;
}
