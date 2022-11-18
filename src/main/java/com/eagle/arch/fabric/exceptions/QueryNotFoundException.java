package com.eagle.arch.fabric.exceptions;

public class QueryNotFoundException extends RuntimeException {

    public QueryNotFoundException(String message) {
        super(message);
    }
}
