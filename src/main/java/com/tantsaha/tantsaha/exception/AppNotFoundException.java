package com.tantsaha.tantsaha.exception;

public class AppNotFoundException extends RuntimeException {
    public AppNotFoundException(String message) {
        super(message);
    }
}
