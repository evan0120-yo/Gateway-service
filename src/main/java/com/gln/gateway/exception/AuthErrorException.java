package com.gln.gateway.exception;

public class AuthErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthErrorException() {
        super("Auth error");
    }

    public AuthErrorException(String msg) {
        super("Auth error: " + msg);
    }
}
