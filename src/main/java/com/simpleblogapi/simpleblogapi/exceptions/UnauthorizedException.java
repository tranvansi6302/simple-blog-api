package com.simpleblogapi.simpleblogapi.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message) {
        super(message);
    }
}
