package com.capstone.unwind.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("The new password and confirm password do not match.");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
