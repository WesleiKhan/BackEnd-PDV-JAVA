package com.example.PDV.EmployeeCore;

import com.example.PDV.Config.ConfigAuth.CustomUserDetails;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeOutDto;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository userRepository;

    public EmployeeService(EmployeeRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void createEmployee(EmployeeEntryDto user) {

        String encryptPassword = new BCryptPasswordEncoder()
                .encode(user.getPassword().trim());

        user.setPassword(encryptPassword);

        EmployeeEntity newEmployee = new EmployeeEntity(user);


        userRepository.save(newEmployee);
    }

    public void updateEmployee(EmployeeEntryDto userUpdate, Integer userId) {

        EmployeeEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        user.updateUser(userUpdate);

        userRepository.save(user);
    }

    public List<EmployeeOutDto> getEmployee() {

        return userRepository.findAllBy();
    }

    public void deleteEmployee(Integer id) {

        EmployeeEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);


        userRepository.delete(user);
    }

    public EmployeeEntity loggedInEmployee() {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

       return userRepository.findById(userDetails.getId())
                .orElseThrow(UserNotFound::new);

    }

    public static Integer currentEmployeeId() {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userDetails.getId();
    }
}
