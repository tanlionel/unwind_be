package com.capstone.unwind.exception;

public class UserDoesNotHavePermission extends Exception{
    @Override
    public String getMessage(){
        return Message.msgInvalidPermission;
    }
}
