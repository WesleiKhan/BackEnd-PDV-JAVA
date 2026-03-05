package com.example.PDV.UsersCore.UserDtos;

import com.example.PDV.UsersCore.Enums.Roles;

public record UserOutDto(Integer id, String name, Roles role) {
}
