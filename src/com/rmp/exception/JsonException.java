package com.rmp.exception;

public class JsonException extends RuntimeException {

    public JsonException(String string) {
        super(string);
    }

    public JsonException(String string, Throwable cause) {
        super(string, cause);
    }
}
