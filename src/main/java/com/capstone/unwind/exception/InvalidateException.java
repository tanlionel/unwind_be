package com.capstone.unwind.exception;

public class InvalidateException extends Exception {
    @Override
    public String getMessage(){
        return Message.msgInvalidCredential;
    }
}
