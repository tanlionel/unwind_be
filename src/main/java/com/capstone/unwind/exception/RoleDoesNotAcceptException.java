package com.capstone.unwind.exception;

public class RoleDoesNotAcceptException extends Exception{
    @Override
    public String getMessage() {
        return Message.getMsgInvalidRole;
    }
}