package com.example.PDV.SaleCore.Entities;

import com.example.PDV.SaleCore.SaleEnums.Installments;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_of_sale")
public class PaymentOfSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "kind_of_payment")
    @Enumerated(EnumType.STRING)
    private KindOfPayment kindOfPayment;

    @Column(name = "total_payment_sale")
    private BigDecimal totalPaymentSale;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleEntity sale;

    @Column(name = "installment")
    @Enumerated(EnumType.STRING)
    private Installments installment;

    @Column(name = "installment_value")
    private String installment_value;

    public PaymentOfSaleEntity() {}

    public PaymentOfSaleEntity(KindOfPayment kind, BigDecimal totalPaymentSale,
                               SaleEntity sale) {

        this.kindOfPayment = kind;
        this.totalPaymentSale = totalPaymentSale;
        this.sale = sale;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KindOfPayment getKindOfPayment() {
        return kindOfPayment;
    }

    public void setKindOfPayment(KindOfPayment kindOfPayment) {
        this.kindOfPayment = kindOfPayment;
    }

    public BigDecimal getTotalPaymentSale() {
        return totalPaymentSale;
    }

    public void setTotalPaymentSale(BigDecimal totalPaymentSale) {
        this.totalPaymentSale = totalPaymentSale;
    }

    public SaleEntity getSale() {
        return sale;
    }

    public void setSale(SaleEntity sale) {
        this.sale = sale;
    }

    public Installments getInstallment() {
        return installment;
    }

    public void setInstallment(Installments installment) {
        this.installment = installment;
    }

    public String getInstallment_value() {
        return installment_value;
    }

    public void setInstallment_value(String installment_value) {
        this.installment_value = installment_value;
    }
}
