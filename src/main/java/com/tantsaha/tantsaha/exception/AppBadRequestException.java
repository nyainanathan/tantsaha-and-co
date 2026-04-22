package com.tantsaha.tantsaha.exception;

import lombok.AllArgsConstructor;

public class AppBadRequestException extends RuntimeException{
    public AppBadRequestException(String message){
        super(message);
    }
}
