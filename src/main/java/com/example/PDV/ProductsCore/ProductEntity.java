package com.example.PDV.ProductsCore;

import com.example.PDV.ProductsCore.ProductDtos.ProductEntryDto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "valueProduct", precision = 10, scale = 2)
    private BigDecimal value;

    public ProductEntity() {
    }

    public ProductEntity(ProductEntryDto entry) {

        if (entry.getProductName() != null && !entry.getProductName().trim().isEmpty()
                && entry.getQuantity() != null && entry.getValue() != null) {

            this.productName = entry.getProductName();
            this.quantity = entry.getQuantity();
            this.value = entry.getValue();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public void updateProduct(ProductEntryDto entry) {

        if (entry.getProductName() != null
                && !entry.getProductName().trim().isEmpty()) {

            this.productName = entry.getProductName();
        }

        if (entry.getQuantity() != null) {

            this.quantity = entry.getQuantity();
        }

        if (entry.getValue() != null) {

            this.value = entry.getValue();
        }
    }
}
