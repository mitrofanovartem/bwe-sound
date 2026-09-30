package com.bwesound.identity.auth;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(){
        super("Неверный логин или пароль");
    }
}
