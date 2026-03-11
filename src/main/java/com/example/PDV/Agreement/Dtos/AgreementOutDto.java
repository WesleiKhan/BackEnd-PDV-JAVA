package com.example.PDV.Agreement.Dtos;

import com.example.PDV.Agreement.Enums.StatusAgreement;
import com.example.PDV.CustomerCore.Dtos.InfosCustomerDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AgreementOutDto(Integer id, InfosCustomerDto customer,
                              LocalDateTime dateStartAgreement,
                              LocalDateTime dateEndAgreement,
                              StatusAgreement status,
                              BigDecimal percentage) {
}
