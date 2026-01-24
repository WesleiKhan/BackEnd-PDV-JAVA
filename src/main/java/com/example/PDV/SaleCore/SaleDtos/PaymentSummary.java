package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;

import java.math.BigDecimal;

public interface PaymentSummary {
    KindOfPayment getKindOfPayment();
    BigDecimal getTotal();
}
