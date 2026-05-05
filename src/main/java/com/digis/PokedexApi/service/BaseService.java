package com.digis.PokedexApi.service;

import com.digis.PokedexApi.dto.Result;
import com.digis.PokedexApi.exception.ErrorCode;
import java.util.List;
import java.util.function.Supplier;

public abstract class BaseService {

    //CUANDO SOLO VOY A RECIBIR UN SOLO OBJECTO (result.object)
    protected Result ejecutar(Supplier<Object> accion) {
        try {
            return Result.ok(accion.get());
        } catch (IllegalArgumentException e) {
            return Result.error(ErrorCode.INVALID_INPUT, e.getLocalizedMessage(), e);
        } catch (RuntimeException e) {
            return Result.error(ErrorCode.NOT_FOUND, e.getLocalizedMessage(), e);
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, "Ocurrio un error en el servidor", e);
        }
    }

    //CUANDO VOY A RECIBIR UNA LISTA (result.objects)
    protected Result ejecutarLista(Supplier<List<?>> accion) {
        try {
            return Result.okList(accion.get());
        } catch (IllegalArgumentException e) {
            return Result.error(ErrorCode.INVALID_INPUT, e.getMessage(), e);
        } catch (RuntimeException e) {
            return Result.error(ErrorCode.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR,
                    "Ocurrió un error inesperado", e);
        }
    }

    protected Result ejecutarVoid(Runnable action) {
        try {
            action.run();
            return Result.ok(true);
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR, e.getLocalizedMessage());
        }
    }
}
