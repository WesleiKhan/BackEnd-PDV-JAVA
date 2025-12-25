package com.example.PDV.ProductsCore.ProductDtos;

import java.math.BigDecimal;

public class ProductEntryDto {

    private String productName;

    private Integer quantity;

    private BigDecimal value;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
