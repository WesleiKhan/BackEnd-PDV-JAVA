package com.example.PDV.Exceptions;

public class BoxNotFound extends RuntimeException{

    public BoxNotFound() {super("Caixa não foi encontrado!");}

    public BoxNotFound(String message) {super(message);}
}
