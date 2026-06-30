package org.example.userauthservice_may2026.exceptions;

public class UserAlreadyException extends RuntimeException{
    public UserAlreadyException(String msg){
        super(msg);
    }
}
