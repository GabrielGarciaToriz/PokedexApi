package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class BaseService {

    //CUANDO SOLO VOY A RECIBIR UN SOLO OBJECTO (result.object)
    protected Result ejecutar(Supplier<Object> accion) {
        Result result = new Result();
        try {
            result.object = accion.get();
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }

    //CUANDO VOY A RECIBIR UNA LISTA (result.objects)
    protected Result ejecutarLista(Supplier<List<?>> accion) {
        Result result = new Result();
        try {
            result.objects = new ArrayList<>(accion.get());
            result.correct = true;
        } catch (Exception e) {
            result.correct = false;
            result.errorMessage = e.getLocalizedMessage();
            result.ex = e;
        }
        return result;
    }
}
