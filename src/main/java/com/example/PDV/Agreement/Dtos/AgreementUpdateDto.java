package com.example.PDV.Agreement.Dtos;

import com.example.PDV.Agreement.Enums.StatusAgreement;

import java.math.BigDecimal;

public class AgreementUpdateDto {

    private BigDecimal percentage;

    private StatusAgreement status;

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
}
