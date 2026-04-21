package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.SaleCore.Entities.ItemsForSaleEntity;

import java.math.BigDecimal;
import java.util.List;

public record CreateItemsForSaleOutDto(List<ItemsForSaleEntity> sales,
                                       BigDecimal valueTotal,
                                       Integer quantity) {
}
