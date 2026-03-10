package com.example.PDV.Agreement.Dtos;

import com.example.PDV.CustomerCore.CustomerEntity;

import java.math.BigDecimal;

public class AgreementEntryDto {

    private BigDecimal percentage;

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
