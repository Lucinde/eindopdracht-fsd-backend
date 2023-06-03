package com.lucinde.plannerpro.exceptions;

public class StringNotFoundException extends RuntimeException {
    public StringNotFoundException() {
        super();
    }

    public StringNotFoundException(String message) {
        super(message);
    }
}
