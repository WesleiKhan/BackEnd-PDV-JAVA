package com.example.PDV.SaleCore.Entities;

import com.example.PDV.BoxCore.BoxEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private BoxEntity box;

    @Column(name = "time_of_sale")
    private LocalDateTime timeOfSale;

    @Column(name = "total_value_sale")
    private BigDecimal totalValueSale;

    @Column(name = "quantity_items")
    private Integer quantity;

    @PrePersist
    public void onCreate() {

        this.timeOfSale = LocalDateTime.now();
    }

    public SaleEntity() {}

    public SaleEntity(BoxEntity boxEntity) {

        this.box = boxEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BoxEntity getBox() {
        return box;
    }

    public void setBox(BoxEntity box) {
        this.box = box;
    }

    public LocalDateTime getTimeOfSale() {
        return timeOfSale;
    }

    public void setTimeOfSale(LocalDateTime timeOfSale) {
        this.timeOfSale = timeOfSale;
    }

    public BigDecimal getTotalValueSale() {
        return totalValueSale;
    }

    public void setTotalValueSale(BigDecimal totalValueSale) {
        this.totalValueSale = totalValueSale;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
