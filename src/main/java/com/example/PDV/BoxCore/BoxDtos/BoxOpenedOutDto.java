package com.example.PDV.BoxCore.BoxDtos;

import com.example.PDV.BoxCore.BoxEntity;
import com.example.PDV.SaleCore.SaleDtos.PaymentSummary;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;

import java.math.BigDecimal;
import java.util.Map;

public record BoxOpenedOutDto(BoxEntity box,
                              Map<KindOfPayment, BigDecimal> payments) {
}
