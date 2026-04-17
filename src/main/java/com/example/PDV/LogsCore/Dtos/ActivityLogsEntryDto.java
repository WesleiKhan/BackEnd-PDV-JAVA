package com.example.PDV.LogsCore.Dtos;


import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;

public class ActivityLogsEntryDto {

    private EntityType entityType;

    private Integer entity_id;

    private TypeAction action;

    private EmployeeEntity employee;

    private String description;

    public ActivityLogsEntryDto(EntityType entityType, Integer entity_id,
                                TypeAction action) {
        this.entityType = entityType;
        this.entity_id = entity_id;
        this.action = action;
    }


    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Integer getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Integer entity_id) {
        this.entity_id = entity_id;
    }

    public TypeAction getAction() {
        return action;
    }

    public void setAction(TypeAction action) {
        this.action = action;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
