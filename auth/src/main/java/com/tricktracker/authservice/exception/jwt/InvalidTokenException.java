package com.tricktracker.authservice.exception.jwt;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
