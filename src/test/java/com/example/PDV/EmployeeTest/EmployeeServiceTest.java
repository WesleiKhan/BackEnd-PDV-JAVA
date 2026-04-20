package com.example.PDV.EmployeeTest;

import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.EmployeeRepository;
import com.example.PDV.EmployeeCore.EmployeeService;
import com.example.PDV.EmployeeCore.Enums.Roles;
import com.example.PDV.Exceptions.UserNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    private EmployeeEntryDto dto;

    private EmployeeEntity employee;

    @BeforeEach
    void setup() {
        dto = new EmployeeEntryDto();
        dto.setName("Boneco test");
        dto.setPassword("123");
        dto.setRole(Roles.ROLE_OPERATOR_BOX);

        employee = new EmployeeEntity(dto);
        employee.setId(1);
    }

    @Test
    void shouldCreateEmployee() {

        employeeService.createEmployee(dto);

        verify(employeeRepository).save(any());
    }

    @Test
    void shouldEncryptPasswordBeforeSaving() {

        employeeService.createEmployee(dto);

        verify(employeeRepository).save(argThat(employee ->
                !employee.getPassword().equals("123")
        ));
    }

    @Test
    void shouldUpdateEmployee() {

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        dto.setRole(Roles.ROLE_MANAGER);

        employeeService.updateEmployee(dto, 1);

        verify(employeeRepository).save(argThat(emp ->
                emp.getRole() == Roles.ROLE_MANAGER));
    }

    @Test
    void shouldNotUpdateNameWhenNull() {

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        String originalName = employee.getName();

        dto.setName(null);

        employeeService.updateEmployee(dto, 1);

        verify(employeeRepository).save(argThat(emp ->
                emp.getName().equals(originalName)));
    }

    @Test
    void shouldNotUpdatePasswordWhenEmpty() {

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        String originalPassword = employee.getPassword();

        dto.setPassword("  ");

        employeeService.updateEmployee(dto, 1);

        verify(employeeRepository).save(argThat(emp ->
                emp.getPassword().equals(originalPassword)));
    }

    @Test
    void shouldDeleteEmployee() {

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {

        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> {
            employeeService.deleteEmployee(1);
        });
    }

}
