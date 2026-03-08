package com.example.PDV.EmployeeCore;

import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeOutDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    List<EmployeeOutDto> findAllBy();

    Optional<EmployeeEntity> findByName(String name);
}
