package com.example.PDV.SaleCore.SaleDtos;

import com.example.PDV.ProductsCore.ProductDtos.ProductOutDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record InfoOfProductsSaleDto(List<ProductOutDto> products,
                                    BigDecimal valueTotal) implements Serializable {
}
