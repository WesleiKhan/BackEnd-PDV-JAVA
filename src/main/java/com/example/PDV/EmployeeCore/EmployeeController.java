package com.example.PDV.EmployeeCore;

import com.example.PDV.Config.ConfigAuth.CustomUserDetails;
import com.example.PDV.Config.ConfigAuth.TokenService;
import com.example.PDV.EmployeeCore.EmployeeDtos.LoginOutDto;
import com.example.PDV.EmployeeCore.EmployeeDtos.Refresh;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeOutDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UserDetailsService userDetailsService;

    public EmployeeController(EmployeeService employeeService,
                              AuthenticationManager authenticationManager,
                              TokenService tokenService,
                              UserDetailsService userDetailsService) {

        this.employeeService = employeeService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> createUser(@RequestBody EmployeeEntryDto user) {

        employeeService.createEmployee(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User Criado Com " +
                "Sucesso!");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> updateUser(@RequestBody EmployeeEntryDto user,
                                             @PathVariable Integer id) {

        employeeService.updateEmployee(user, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<EmployeeOutDto>> viewUsers() {

        List<EmployeeOutDto> users = employeeService.getEmployee();

        return ResponseEntity.ok().body(users);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOutDto> login(@RequestBody EmployeeEntryDto entry ) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(
                entry.getName(), entry.getPassword());

        var auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generationToken((CustomUserDetails) auth.getPrincipal());

        String refreshToken = tokenService.generateRefreshToken((CustomUserDetails) auth.getPrincipal());

        return ResponseEntity.ok().body(new LoginOutDto(token, refreshToken));
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginOutDto> refresh(@RequestBody Refresh refresh) {

        String name = tokenService.validationToken(refresh.getTokenRefresh());
        var user = userDetailsService.loadUserByUsername(name);

        String newAccess = tokenService.generateRefreshToken((CustomUserDetails) user);

        return ResponseEntity.ok().body(new LoginOutDto(newAccess,
                refresh.getTokenRefresh()));
    }
}
