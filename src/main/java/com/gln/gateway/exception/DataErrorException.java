package com.gln.gateway.exception;

public class DataErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataErrorException() {
        super("Data not found");
    }

    public DataErrorException(String msg) {
        super("Data not found: " + msg);
    }
}
