package com.digis.PokedexApi.exception;

public class ErrorCode {

    private ErrorCode() {
    }

    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String DUPLICATE = "DUPLICATE";
    public static final String INVALID_INPUT = "INVALID_INPUT";
    public static final String EXTERNAL_API = "EXTERNAL_API_ERROR";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
}
