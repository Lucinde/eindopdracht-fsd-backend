package com.lucinde.plannerpro.exceptions;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException() {
        super();
    }

    public ContentNotFoundException(String message) {
        super(message);
    }
}
