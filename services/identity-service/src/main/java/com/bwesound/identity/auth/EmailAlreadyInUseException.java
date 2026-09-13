package com.bwesound.identity.auth;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException(){
        super("Пользователь с таким email уже существует");
    }
}
