package com.example.PDV.LogsCore;

import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.EmployeeService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import org.springframework.stereotype.Service;

@Service
public class ActivityLogsService {

    private final ActivityLogsRepository activityLogsRepository;

    private final EmployeeService employeeService;

    public ActivityLogsService(ActivityLogsRepository activityLogsRepository
            ,EmployeeService employeeService) {

        this.activityLogsRepository = activityLogsRepository;
        this.employeeService = employeeService;

    }

    public void createActivityLogs(ActivityLogsEntryDto entry) {

        EmployeeEntity employee = employeeService.loggedInEmployee();

        String descriptio = createDescription(employee,
                entry.getEntityType(), entry.getAction());

        entry.setDescription(descriptio);
        entry.setEmployee(employee);

        ActivityLogEntity activityLog = new ActivityLogEntity(entry);

        activityLogsRepository.save(activityLog);
    }

    private String createDescription(EmployeeEntity employee,
                                     EntityType entity,
                                     TypeAction action) {

        if (action == null) {
            throw new IllegalArgumentException("Action não pode ser null");
        }

        switch (action) {
            case TypeAction.CREATE -> {
                return String.format("O Operador De Caixa: %s, Realizou a " +
                        "criacao " +
                        "de um " +
                        "novo(a) %s", employee.getName(), entity);
            }

            case TypeAction.UPDATE -> {
                return String.format("O Operador De Caixa: %s, Realizou a " +
                                "Alteracoes em %s", employee.getName(),
                        entity);
            }
            case TypeAction.DELETE -> {
                return String.format("O Operador De Caixa: %s, Excluiu " +
                                "Informacoes de " +
                                " %s",
                        employee.getName(), entity);
            }
            case TypeAction.SALE -> {
                return String.format("O Operador De Caixa: %s, Realizou uma venda",
                        employee.getName());
            }
            case TypeAction.SALE_CANCEL-> {
                  return String.format("O Operador De Caixa: %s, Realizou  " +
                        "Cancelamento de uma venda", employee.getName());
            }
            case TypeAction.FINISHED_BOX -> {
                return String.format("O Operador De Caixa: %s, Realizou  " +
                        "o encerramento do caixa", employee.getName());
            }


            default -> throw new IllegalArgumentException("Ação não " +
                    "suportada: " + action);
        }
    }
}
