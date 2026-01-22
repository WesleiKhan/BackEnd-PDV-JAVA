package com.example.PDV.Exceptions;

public class OperatorAlreadyBoxOpened extends RuntimeException {

    public OperatorAlreadyBoxOpened() {super("O Operado ja tem um caixa em " +
            "aberto!");}

    public OperatorAlreadyBoxOpened(String message) {
        super(message);
    }
}
