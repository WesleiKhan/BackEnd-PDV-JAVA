package com.example.PDV.ConfigAuth;

import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.UsersCore.UserEntity;
import com.example.PDV.UsersCore.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFound("Voce ainda não e " +
                        "cadastrado, por favor realize o cadastro antes de " +
                        "tentar o login novamnete!"));

        return new CustomUserDetails(user.getId(), user.getName(),
                user.getPassword());
    }
}
