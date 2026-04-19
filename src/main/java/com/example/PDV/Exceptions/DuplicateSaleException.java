package com.example.PDV.Exceptions;

public class DuplicateSaleException extends RuntimeException {

    public DuplicateSaleException() {
        super("Venda já processada");
    }

    public DuplicateSaleException(String message) {

        super(message);
    }
}
