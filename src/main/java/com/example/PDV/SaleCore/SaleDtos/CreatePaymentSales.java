package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.SaleCore.Entities.PaymentOfSaleEntity;

import java.math.BigDecimal;
import java.util.List;

public record CreatePaymentSales(List<PaymentOfSaleEntity> payments,
                                 BigDecimal totalPrincePayment) {
}
