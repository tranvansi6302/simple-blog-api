package com.simpleblogapi.simpleblogapi.exceptions;

public class PermissionDeniedException extends Exception{
    public PermissionDeniedException(String message) {
        super(message);
    }
}
