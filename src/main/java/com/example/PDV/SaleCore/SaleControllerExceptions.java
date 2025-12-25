package com.example.PDV.SaleCore;

import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.PriceIsNotEqual;
import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.Exceptions.SaleNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class SaleControllerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BoxNotFound.class)
    private ResponseEntity<String> HandlerBoxNotFound(BoxNotFound message) {

        Map<String, String> response = new HashMap<>();
        response.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.get("message"));
    }

    @ExceptionHandler(ProductNotFound.class)
    private ResponseEntity<String> HandlerProductNotFound(ProductNotFound message) {

        Map<String, String> response = new HashMap<>();
        response.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.get("message"));
    }

    @ExceptionHandler(SaleNotFound.class)
    private ResponseEntity<String> HandlerSaleNotFound(SaleNotFound message) {

        Map<String, String> response = new HashMap<>();
        response.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.get("message"));
    }

    @ExceptionHandler(PriceIsNotEqual.class)
    private ResponseEntity<String> HandlerPriceIsNotEquals(PriceIsNotEqual message) {

        Map<String, String> response = new HashMap<>();
        response.put("message", message.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response.get("message"));
    }
}
