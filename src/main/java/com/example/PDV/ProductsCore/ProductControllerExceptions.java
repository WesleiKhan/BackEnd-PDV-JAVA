package com.example.PDV.ProductsCore;

import com.example.PDV.Exceptions.ProductNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProductControllerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFound.class)
    private ResponseEntity<String> HandlerProductNotFound(ProductNotFound message) {

        Map<String, String> response = new HashMap<>();
        response.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.get("message"));
    }
}
