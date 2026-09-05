package com.tricktracker.authservice.exception.jwt;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
