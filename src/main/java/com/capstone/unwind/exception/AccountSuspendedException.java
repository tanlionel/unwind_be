package com.capstone.unwind.exception;

public class AccountSuspendedException extends Exception {
    @Override
    public String getMessage(){
        return Message.msgAccountSuspended;
    }
}