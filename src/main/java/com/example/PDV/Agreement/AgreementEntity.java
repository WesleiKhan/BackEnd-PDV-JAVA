package com.example.PDV.Agreement;

import com.example.PDV.Agreement.Dtos.AgreementEntryDto;
import com.example.PDV.Agreement.Dtos.AgreementUpdateDto;
import com.example.PDV.Agreement.Enums.StatusAgreement;
import com.example.PDV.CustomerCore.CustomerEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agreements")
public class AgreementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true, referencedColumnName =
            "id")
    private CustomerEntity customer;

    @Column(name = "date_start_agreement")
    private LocalDateTime dateStartAgreement;

    @Column(name = "date_end_agreement")
    private LocalDateTime dateEndAgreement;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusAgreement status;

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;


    @PrePersist
    public void prePersist() {

        this.dateStartAgreement = LocalDateTime.now();
        this.status = StatusAgreement.ATIVO;
    }

    public AgreementEntity() {

    }

    public AgreementEntity(AgreementEntryDto entry) {

        this.percentage = entry.getPercentage();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public LocalDateTime getDateStartAgreement() {
        return dateStartAgreement;
    }

    public void setDateStartAgreement(LocalDateTime dateStartAgreement) {
        this.dateStartAgreement = dateStartAgreement;
    }

    public LocalDateTime getDateEndAgreement() {
        return dateEndAgreement;
    }

    public void setDateEndAgreement(LocalDateTime dateEndAgreement) {
        this.dateEndAgreement = dateEndAgreement;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public StatusAgreement getStatus() {
        return status;
    }

    public void setStatus(StatusAgreement status) {
        this.status = status;
    }

    public void updateAgreement(AgreementUpdateDto entryUpdate) {

        if (entryUpdate.getPercentage() != null) {
            this.percentage = entryUpdate.getPercentage();
        }

        if (entryUpdate.getStatus() != null) {
            this.status = entryUpdate.getStatus();
        }
    }
}
