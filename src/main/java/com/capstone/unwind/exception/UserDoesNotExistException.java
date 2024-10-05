package com.capstone.unwind.exception;

public class UserDoesNotExistException extends Exception{
    @Override
    public String getMessage(){
        return Message.msgUserDoesNotExist;
    }
}
