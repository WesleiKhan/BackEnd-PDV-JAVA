package com.example.PDV.UsersCore;

import com.example.PDV.ConfigAuth.CustomUserDetails;
import com.example.PDV.ConfigAuth.TokenService;
import com.example.PDV.UsersCore.UserDtos.LoginOutDto;
import com.example.PDV.UsersCore.UserDtos.Refresh;
import com.example.PDV.UsersCore.UserDtos.UserEntryDto;
import com.example.PDV.UsersCore.UserDtos.UserOutDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UserDetailsService userDetailsService;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          UserDetailsService userDetailsService) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserEntryDto user) {

        userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User Criado Com " +
                "Sucesso!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody UserEntryDto user,
                                             @PathVariable Integer id) {

        userService.updateUser(user, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDto>> viewUsers() {

        List<UserOutDto> users = userService.getUsers();

        return ResponseEntity.ok().body(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {

        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOutDto> login(@RequestBody UserEntryDto entry ) {

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
