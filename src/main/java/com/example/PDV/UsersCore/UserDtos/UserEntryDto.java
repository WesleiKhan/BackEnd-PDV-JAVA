package com.example.PDV.UsersCore.UserDtos;

import com.example.PDV.UsersCore.Enums.Roles;

public class UserEntryDto {

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
