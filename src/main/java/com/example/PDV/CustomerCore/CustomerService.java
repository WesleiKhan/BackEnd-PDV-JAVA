package com.example.PDV.CustomerCore;

import com.example.PDV.CustomerCore.Dtos.CustomerEntryDto;
import com.example.PDV.CustomerCore.Dtos.CustomerOutDto;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final ActivityLogsService activityLogsService;

    public CustomerService(CustomerRepository customerRepository,
                           ActivityLogsService activityLogsService) {

        this.customerRepository = customerRepository;
        this.activityLogsService = activityLogsService;
    }

    public void createCustomer (CustomerEntryDto entry) {

        CustomerEntity newCustomer = new CustomerEntity(entry.getName(),
                entry.getPhoneNumber());

        newCustomer.endRegister(entry);

        customerRepository.save(newCustomer);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.CUSTOMER,
                newCustomer.getId(), TypeAction.CREATE));
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

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.CUSTOMER,
                customer.getId(), TypeAction.UPDATE));

    }

    public void deleteCustomer(Integer id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        customerRepository.delete(customer);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.CUSTOMER,
                customer.getId(), TypeAction.DELETE));
    }
}
