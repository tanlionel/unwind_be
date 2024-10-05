package com.capstone.unwind.exception;

public class RoleDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return Message.msgRoleDoesNotExist;
    }
}