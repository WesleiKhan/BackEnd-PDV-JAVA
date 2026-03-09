package com.example.PDV.CustomerCore;

import com.example.PDV.CustomerCore.Dtos.CustomerEntryDto;
import com.example.PDV.CustomerCore.Dtos.CustomerOutDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerEntryDto entry) {

        customerService.createCustomer(entry);

        return ResponseEntity.ok().body("Cliente registrado com sucesso");
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerOutDto>> getCustomers() {

        return ResponseEntity.ok().body(customerService.getCustomers());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerEntryDto entry, @PathVariable Integer id) {

        customerService.updateCustomer(entry, id);

        return ResponseEntity.ok().body("Dados Do Cliente Atualizados com " +
                "sucesso");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.ok().body("Funcionario deletado com sucesso");
    }
}
