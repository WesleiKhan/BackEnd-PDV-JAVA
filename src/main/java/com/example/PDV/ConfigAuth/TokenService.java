package com.example.PDV.ConfigAuth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generationToken(CustomUserDetails customUserDetails) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("PDV-api")
                    .withSubject(customUserDetails.getUsername())
                    .withExpiresAt(genExperationDateMinute())
                    .sign(algorithm);

            return token;
        }catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token: " + e.getMessage());
        }
    }

    private Instant genExperationDateMinute() {

        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of(
                "-03" +
                ":00"));
    }

    public String validationToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("PDV-api")
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (JWTVerificationException e) {
            throw new RuntimeException("Erro ao validar token: " + e.getMessage());
        }
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("PDV-api")
                    .withSubject(customUserDetails.getUsername())
                    .withExpiresAt(genExperationDateDays())
                    .sign(algorithm);

        } catch (JWTCreationException e) {

            throw new RuntimeException("Erro ao gerar refresh token: " + e.getMessage());
        }
    }

    private Instant genExperationDateDays() {

        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03" +
                ":00"));
    }
}
