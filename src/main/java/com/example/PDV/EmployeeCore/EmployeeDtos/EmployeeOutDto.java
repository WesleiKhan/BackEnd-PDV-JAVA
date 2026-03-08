package com.example.PDV.EmployeeCore.EmployeeDtos;

import com.example.PDV.EmployeeCore.Enums.Roles;

public record EmployeeOutDto(Integer id, String name, Roles role) {
}
