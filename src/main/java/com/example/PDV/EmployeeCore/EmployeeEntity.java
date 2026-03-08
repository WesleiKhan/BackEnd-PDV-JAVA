package com.example.PDV.EmployeeCore;

import com.example.PDV.EmployeeCore.Enums.Roles;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Roles role;

    public EmployeeEntity() {

    }

    public EmployeeEntity(EmployeeEntryDto user) {

        if (user.getName() != null && !user.getName().trim().isEmpty()
                && user.getPassword() != null && !user.getPassword().trim().isEmpty()
        && user.getRole() != null) {

            this.name = user.getName();
            this.password = user.getPassword();
            this.role = user.getRole();
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

    public void updateUser(EmployeeEntryDto user) {

        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            this.name = user.getName();
        }

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            this.password = user.getPassword();
        }

        if (user.getRole() !=null) {
            this.role = user.getRole();
        }
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
