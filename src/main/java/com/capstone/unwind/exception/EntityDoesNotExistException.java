package com.capstone.unwind.exception;

public class EntityDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return Message.msgEntityDoesNotExist;
    }
}