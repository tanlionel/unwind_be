package com.capstone.unwind.exception;

public class EntityAlreadyExist extends Exception{
    @Override
    public String getMessage() {
        return Message.msgEntityAlreadyExist;
    }
}