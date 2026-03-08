package com.example.PDV.Config.ConfigAuth;

import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.EmployeeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public AuthorizationService(EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @Cacheable(
            value = "employee",
            key = "#username",
            unless = "#result == null"
    )
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("CHAMADA AO BANCO DE DADO");

        EmployeeEntity employee = employeeRepository.findByName(username)
                .orElseThrow(() -> new UserNotFound("Voce ainda não e " +
                        "cadastrado, por favor realize o cadastro antes de " +
                        "tentar o login novamnete!"));

        System.out.println("ROLE: " + employee.getRole());

        return new CustomUserDetails(employee.getId(),
                employee.getName(),
                employee.getPassword(),
                employee.getRole().name());

    }
}
