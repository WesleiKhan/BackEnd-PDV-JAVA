package com.example.PDV.UsersCore;

import com.example.PDV.Exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserControllerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    private ResponseEntity<String> UserNotFound(UserNotFound message) {

        Map<String, String> erro = new HashMap<>();
        erro.put("Message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(erro.get("Message"));
    }
}
