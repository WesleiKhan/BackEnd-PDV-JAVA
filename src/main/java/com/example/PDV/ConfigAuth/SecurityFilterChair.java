package com.example.PDV.ConfigAuth;

import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.UsersCore.UserEntity;
import com.example.PDV.UsersCore.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilterChair extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    public SecurityFilterChair(TokenService tokenService,
                               UserRepository userRepository) {

        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        return path.equals("/user/login") || path.equals("/user/refresh");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        var token = recoverToken(request);

        if (token != null) {

            try {

                var subject = tokenService.validationToken(token);

                UserEntity user = userRepository.findByName(subject)
                        .orElseThrow(UserNotFound::new);

                CustomUserDetails userDetails = new CustomUserDetails(user.getId(),
                        user.getName(), user.getPassword());

                var authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);

    }


    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) return null;

        return authHeader.replace("Bearer ", "");
    }
}
