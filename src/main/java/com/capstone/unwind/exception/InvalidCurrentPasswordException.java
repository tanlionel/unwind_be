package com.capstone.unwind.exception;

public class InvalidCurrentPasswordException extends RuntimeException {

    public InvalidCurrentPasswordException() {
        super("The current password is incorrect.");
    }
    public InvalidCurrentPasswordException(String message) {
        super(message);
    }
}
