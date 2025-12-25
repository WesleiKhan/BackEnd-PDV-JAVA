package com.example.PDV.SaleCore.Entities;

import com.example.PDV.ProductsCore.ProductEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "items_for_sales")
public class ItemsForSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleEntity sale;

    public ItemsForSaleEntity() {}

    public ItemsForSaleEntity(ProductEntity product,
                              SaleEntity sale) {

        this.product = product;
        this.sale = sale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public SaleEntity getSale() {
        return sale;
    }

    public void setSale(SaleEntity sale) {
        this.sale = sale;
    }
}
