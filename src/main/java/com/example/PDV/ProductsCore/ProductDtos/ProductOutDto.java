package com.example.PDV.ProductsCore.ProductDtos;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductOutDto(Integer id, String productName,
                            BigDecimal value) implements Serializable {
}
