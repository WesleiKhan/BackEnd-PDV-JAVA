package com.example.PDV.SaleCore.SaleEnums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Installments {

    UMA(1),
    DUAS(2),
    TRES(3),
    QUATRO(4),
    CINCO(5),
    SEIS(6),
    SETE(7),
    OITO(8),
    NOVE(9),
    DEZ(10),
    ONZE(11),
    DOZE(12);

    private final int quantity;

    Installments(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @JsonCreator
    public static Installments fromValue(Object value) {

        if (value instanceof Integer) {

            int intValue = (Integer) value;

            for (Installments i : Installments.values()) {

                if (i.getQuantity() == intValue) return i;
            }
        }

        if (value instanceof String) {
            return Installments.valueOf(((String) value).toUpperCase());
        }

        throw new IllegalArgumentException("Parcela Invalida!");
    }
}
