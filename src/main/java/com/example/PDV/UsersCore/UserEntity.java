package com.example.PDV.UsersCore;

import com.example.PDV.UsersCore.UserDtos.UserEntryDto;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    public UserEntity() {

    }

    public UserEntity(UserEntryDto user) {

        if (user.getName() != null && !user.getName().trim().isEmpty()
                && user.getPassword() != null && !user.getPassword().trim().isEmpty()) {

            this.name = user.getName();
            this.password = user.getPassword();
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {return password;}

    private void setName(String name) {
        this.name = name;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public void updateUser(UserEntryDto user) {

        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            this.name = user.getName();
        }

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            this.password = user.getPassword();
        }
    }
}
