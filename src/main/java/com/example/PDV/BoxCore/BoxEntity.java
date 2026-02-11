package com.example.PDV.BoxCore;

import com.example.PDV.BoxCore.BoxEnums.StatusBox;
import com.example.PDV.UsersCore.UserEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Box")
public class BoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "start_date", nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "total_value", precision = 10, scale = 2)
    private BigDecimal totalValue;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private UserEntity operator;

    @Enumerated(EnumType.STRING)
    private StatusBox status_of_box = StatusBox.OPEN;

    @PrePersist
    public void prePersist() {

        this.startDate = LocalDateTime.now();
        this.totalValue = new BigDecimal(0);
    }

    public BoxEntity() {}

    public BoxEntity(UserEntity opeartor) {

        this.operator = opeartor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        if (endDate != null) {
            this.endDate = endDate;
        }
    }

    public UserEntity getOperator() {
        return operator;
    }

    public void setOperator(UserEntity operator) {
        this.operator = operator;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {

        if (totalValue != null) {
            this.totalValue = this.totalValue.add(totalValue);
        }
    }

    public StatusBox getStatus_of_box() {
        return status_of_box;
    }

    public void setStatus_of_box(StatusBox status_of_box) {
        this.status_of_box = status_of_box;
    }
}
