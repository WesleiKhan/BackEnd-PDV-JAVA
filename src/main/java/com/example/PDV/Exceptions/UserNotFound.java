package com.example.PDV.Exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound() {super("Usuario não foi encontrado!");}

    public UserNotFound(String message) {super(message);}
}
