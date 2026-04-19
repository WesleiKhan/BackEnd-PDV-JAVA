package com.example.PDV.Exceptions;

public class ProductNotFound extends BusinessException{

    public ProductNotFound() {super("Produto não foi encontrado!");}

    public ProductNotFound(String message) {super(message);}
}
