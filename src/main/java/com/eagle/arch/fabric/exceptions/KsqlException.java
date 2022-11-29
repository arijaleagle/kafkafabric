package com.eagle.arch.fabric.exceptions;

public class KsqlException extends RuntimeException {
    public KsqlException(String message) {
        super(message);
    }
}