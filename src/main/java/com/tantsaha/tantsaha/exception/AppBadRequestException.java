package com.tantsaha.tantsaha.exception;

import lombok.AllArgsConstructor;

public class AppBadRequestException extends RuntimeException{
    private String message;

    public AppBadRequestException(String message){
        super(message);
    }
}
