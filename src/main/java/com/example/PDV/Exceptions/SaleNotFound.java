package com.example.PDV.Exceptions;

public class SaleNotFound extends RuntimeException{

    public SaleNotFound() {super("Vendar não foi encontrada!");}

    public SaleNotFound(String message) {super(message);}
}
