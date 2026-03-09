package com.example.PDV.CustomerCore;

import com.example.PDV.CustomerCore.Dtos.CustomerEntryDto;
import com.example.PDV.CustomerCore.Dtos.CustomerOutDto;
import com.example.PDV.Exceptions.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public void createCustomer (CustomerEntryDto entry) {

        CustomerEntity newCustomer = new CustomerEntity(entry.getName(),
                entry.getPhoneNumber());

        newCustomer.endRegister(entry);

        customerRepository.save(newCustomer);
    }

    public List<CustomerOutDto> getCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(c -> new CustomerOutDto(
                        c.getId(),
                        c.getName(),
                        c.getCpf(),
                        c.getCnpj(),
                        c.getPhoneNumber(),
                        c.getTypeCustomer()
                        )
                ).toList();
    }

    public void updateCustomer(CustomerEntryDto entry, Integer id) {

        CustomerEntity customer = customerRepository.findById(id)
                        .orElseThrow(UserNotFound::new);

        customer.updateCustomer(entry);

        customerRepository.save(customer);

    }

    public void deleteCustomer(Integer id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        customerRepository.delete(customer);
    }
}
