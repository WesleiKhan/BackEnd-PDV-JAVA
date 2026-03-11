package com.example.PDV.CustomerCore.Dtos;

import java.io.Serializable;

public record InfosCustomerDto(String name, String cpf, String cnpj,
                               String phoneNumber) implements Serializable {
}
