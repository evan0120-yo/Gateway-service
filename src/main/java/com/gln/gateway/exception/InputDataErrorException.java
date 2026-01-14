package com.gln.gateway.exception;

public class InputDataErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InputDataErrorException() {
        super("Input data error");
    }

    public InputDataErrorException(String msg) {
        super("Input data error: " + msg);
    }
}
