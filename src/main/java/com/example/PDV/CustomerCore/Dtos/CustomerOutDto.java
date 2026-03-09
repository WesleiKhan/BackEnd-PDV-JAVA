package com.example.PDV.CustomerCore.Dtos;

import com.example.PDV.CustomerCore.Enums.TypeCustomer;

public record CustomerOutDto(Integer id, String name, String cpf, String cnpj,
                             String phoneNumber, TypeCustomer typeCustomer) {
}
