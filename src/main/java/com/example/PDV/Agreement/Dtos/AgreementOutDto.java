package com.example.PDV.Agreement.Dtos;

import com.example.PDV.Agreement.Enums.StatusAgreement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AgreementOutDto(Integer id, InfosCustomerOfAgreement customer,
                              LocalDateTime dateStartAgreement,
                              LocalDateTime dateEndAgreement,
                              StatusAgreement status,
                              BigDecimal percentage) {
}
