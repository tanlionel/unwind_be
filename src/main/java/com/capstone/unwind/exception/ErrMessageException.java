package com.capstone.unwind.exception;

public class ErrMessageException extends Exception {
    private String message;

    public ErrMessageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
