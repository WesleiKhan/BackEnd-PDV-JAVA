package com.example.PDV.LogsTest;

import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.EmployeeService;
import com.example.PDV.EmployeeCore.Enums.Roles;
import com.example.PDV.LogsCore.ActivityLogsRepository;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLogsServiceTest {

    @Mock
    ActivityLogsRepository activityLogsRepository;

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    ActivityLogsService activityLogsService;

    private ActivityLogsEntryDto dto;

    private EmployeeEntity operator;

    @BeforeEach
    void setUp() {

        dto = new ActivityLogsEntryDto(EntityType.SALE, 1, TypeAction.SALE);

        EmployeeEntryDto entry = new EmployeeEntryDto();
        entry.setName("operator");
        entry.setPassword("123");
        entry.setRole(Roles.ROLE_OPERATOR_BOX);

        operator = new EmployeeEntity(entry);
    }

    @Test
    void shouldCreateActivityLogs() {

        when(employeeService.loggedInEmployee()).thenReturn(operator);

        activityLogsService.createActivityLogs(dto);

        verify(activityLogsRepository).save(argThat(log ->
                log.getEmployee().equals(operator) &&
                log.getDescription().contains("Realizou uma venda")));
    }

    @Test
    void shouldThrowExceptionForUnsupportedAction() {

        when(employeeService.loggedInEmployee()).thenReturn(operator);

        dto.setAction(null);

        assertThrows(IllegalArgumentException.class, () ->
                activityLogsService.createActivityLogs(dto)
        );
    }

}
