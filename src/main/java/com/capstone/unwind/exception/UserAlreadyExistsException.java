package com.capstone.unwind.exception;

public class UserAlreadyExistsException extends Exception{
    @Override
    public String getMessage(){
        return Message.msgUserAlreadyExist;
    }
}
