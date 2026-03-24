package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.SaleCore.SaleEnums.Installments;

import java.math.BigDecimal;
import java.util.Map;

public class ValueAndInstallments {

    BigDecimal valuePayment;

    Installments installments;

    public BigDecimal getValuePayment() {
        return valuePayment;
    }

    public void setValuePayment(BigDecimal value) {
        this.valuePayment = value;
    }

    public Installments getInstallments() {
        return installments;
    }

    public void setInstallments(Installments installment) {
        this.installments = installment;
    }
}
