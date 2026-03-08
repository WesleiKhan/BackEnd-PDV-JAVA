package com.example.PDV.BoxCore.BoxDtos;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record BoxOpenedOutDto (Integer id,
                              LocalDateTime startDate,
                              String operator,
                              BigDecimal totalValue,
                              Map<KindOfPayment, BigDecimal> payments) implements Serializable {
}
