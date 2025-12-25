package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;

import java.math.BigDecimal;
import java.util.Map;

public class InfoPaymentDto {

    private Map<KindOfPayment, BigDecimal> infoPayment;

    public Map<KindOfPayment, BigDecimal> getInfoPayment() {
        return infoPayment;
    }

    public void setInfoPayment(Map<KindOfPayment, BigDecimal> infoPayment) {
        this.infoPayment = infoPayment;
    }
}
