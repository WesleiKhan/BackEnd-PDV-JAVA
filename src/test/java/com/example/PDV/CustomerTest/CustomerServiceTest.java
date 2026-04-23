package com.example.PDV.CustomerTest;

import com.example.PDV.CustomerCore.CustomerEntity;
import com.example.PDV.CustomerCore.CustomerRepository;
import com.example.PDV.CustomerCore.CustomerService;
import com.example.PDV.CustomerCore.Dtos.CustomerEntryDto;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ActivityLogsService activityLogsService;

    @InjectMocks
    CustomerService customerService;

    private CustomerEntity customer;

    private CustomerEntryDto dtoEntry;

    @BeforeEach
    void setUp() {

        dtoEntry = new CustomerEntryDto();
        dtoEntry.setName("customer-test");
        dtoEntry.setPhoneNumber("99 9 99999999");
        dtoEntry.setCnpj("XXXXXXXXXXXXXX");
        dtoEntry.setCpf(null);

        customer = new CustomerEntity(dtoEntry.getName(),
                dtoEntry.getPhoneNumber());
        customer.setId(1);
        customer.endRegister(dtoEntry);

    }

    @Test
    void shouldCreateCustomer() {

        customerService.createCustomer(dtoEntry);

        verify(customerRepository).save(argThat(cus ->
                cus.getName().equals(dtoEntry.getName()) &&
                        cus.getPhoneNumber().equals(dtoEntry.getPhoneNumber()) &&
                        cus.getCnpj().equals(dtoEntry.getCnpj()) &&
                        cus.getCpf() == null
        ));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldUpdateCustomer() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        String newPhoneNumber = "88 8 99999999";

        dtoEntry.setPhoneNumber(newPhoneNumber);

        customerService.updateCustomer(dtoEntry, 1);

        verify(customerRepository).save(argThat(cus ->
                cus.getPhoneNumber().equals(newPhoneNumber)));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldNotUpdateWhenNameEmptyOrNull() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        String originalName = customer.getName();

        dtoEntry.setName(" ");

        customerService.updateCustomer(dtoEntry, 1);

        verify(customerRepository).save(argThat(cus ->
                cus.getName().equals(originalName)));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldNotUpdateWhenPhoneNumberEmptyOrNull() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        String originalPhoneNumber = customer.getPhoneNumber();

        dtoEntry.setPhoneNumber(" ");

        customerService.updateCustomer(dtoEntry, 1);

        verify(customerRepository).save(argThat(cus ->
                cus.getPhoneNumber().equals(originalPhoneNumber)));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldNotUpdateWhenCnpjEmptyOrNull() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        String originalCnpj = customer.getCnpj();

        dtoEntry.setCnpj(" ");

        customerService.updateCustomer(dtoEntry, 1);

        verify(customerRepository).save(argThat(cus ->
                cus.getCnpj().equals(originalCnpj)));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldNotUpdateWhenCpfEmptyOrNull() {

        customer.setCpf("12223345562");

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        String originalCpf = customer.getCpf();

        dtoEntry.setCpf(" ");

        customerService.updateCustomer(dtoEntry, 1);

        verify(customerRepository).save(argThat(cus ->
                cus.getCpf().equals(originalCpf)));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldDeleteCustomer() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1);

        verify(customerRepository).delete(customer);

        verify(activityLogsService).createActivityLogs(any());

    }

    @Test
    void shouldThrowUserNotFound() {

        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> {
            customerService.deleteCustomer(1);
        });
    }
}



