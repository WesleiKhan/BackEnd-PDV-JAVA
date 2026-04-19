package com.example.PDV.Exceptions;

public class PriceIsNotEqual extends BusinessException {

    public PriceIsNotEqual() {super("O Valor total do pagamneto não e " +
            "igual a soma total dos valores dos produtos!");}

    public PriceIsNotEqual(String message) {
        super(message);
    }
}
