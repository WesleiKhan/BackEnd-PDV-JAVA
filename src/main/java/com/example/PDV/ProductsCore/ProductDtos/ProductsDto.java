package com.example.PDV.ProductsCore.ProductDtos;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductsDto(Integer id, String productName, Integer quantity,
                          BigDecimal value) implements Serializable {
}
