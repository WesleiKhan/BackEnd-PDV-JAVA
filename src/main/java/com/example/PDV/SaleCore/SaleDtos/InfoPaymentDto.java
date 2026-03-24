package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;

import java.math.BigDecimal;
import java.util.Map;

public class InfoPaymentDto {

    private Map<KindOfPayment, ValueAndInstallments> infoPayment;

    public Map<KindOfPayment, ValueAndInstallments> getInfoPayment() {
        return infoPayment;
    }

    public void setInfoPayment(Map<KindOfPayment, ValueAndInstallments> infoPayment) {
        this.infoPayment = infoPayment;
    }
}
