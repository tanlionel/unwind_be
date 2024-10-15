package com.capstone.unwind.exception;

public class OptionalNotFoundException extends Exception {
    private String message;

    public OptionalNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
