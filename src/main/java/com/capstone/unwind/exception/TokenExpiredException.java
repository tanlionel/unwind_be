package com.capstone.unwind.exception;

public class TokenExpiredException extends Exception {
    @Override
    public String getMessage() {
        return Message.msgTokenExpired;
    }
}
