package com.example.PDV.BoxCore;

import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BoxControllerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    private ResponseEntity<String> HandlerUserNotFound(UserNotFound message) {

        Map<String, String> erroApi = new HashMap<>();
        erroApi.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(erroApi.get("message"));
    }

    @ExceptionHandler(BoxNotFound.class)
    private ResponseEntity<String> HandlerBoxNotFound(BoxNotFound message) {

        Map<String, String> erroApi = new HashMap<>();
        erroApi.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(erroApi.get("message"));
    }
}
