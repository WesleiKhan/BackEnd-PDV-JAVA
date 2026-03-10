package com.example.PDV.Agreement.Exceptions;

public class AgreementNotFound extends RuntimeException {

    public AgreementNotFound() {super("Convenio não foi encontrado");}

    public AgreementNotFound(String message) {
        super(message);
    }
}
