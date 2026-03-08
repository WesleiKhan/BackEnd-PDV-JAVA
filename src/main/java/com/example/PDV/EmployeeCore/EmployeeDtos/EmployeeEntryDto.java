package com.example.PDV.EmployeeCore.EmployeeDtos;

import com.example.PDV.EmployeeCore.Enums.Roles;

public class EmployeeEntryDto {

    private String name;

    private String password;

    private Roles role;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
