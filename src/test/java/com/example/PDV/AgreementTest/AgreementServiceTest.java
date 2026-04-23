package com.example.PDV.AgreementTest;

import com.example.PDV.Agreement.AgreementEntity;
import com.example.PDV.Agreement.AgreementRepository;
import com.example.PDV.Agreement.AgreementService;
import com.example.PDV.Agreement.Dtos.AgreementEntryDto;
import com.example.PDV.Agreement.Dtos.AgreementUpdateDto;
import com.example.PDV.Agreement.Enums.StatusAgreement;
import com.example.PDV.Agreement.Exceptions.AgreementNotFound;
import com.example.PDV.CustomerCore.CustomerEntity;
import com.example.PDV.CustomerCore.CustomerRepository;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Enums.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AgreementServiceTest {

    @Mock
    AgreementRepository agreementRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ActivityLogsService activityLogsService;

    @InjectMocks
    AgreementService agreementService;

    private AgreementEntity agreement;

    private CustomerEntity customer;

    private AgreementEntryDto dtoEntry;

    private AgreementUpdateDto dtoUpdate;

    @BeforeEach
    void setUp() {

        customer = new CustomerEntity("Boneco-test", "99 9 99999999");
        customer.setId(1);

        dtoEntry = new AgreementEntryDto();
        dtoEntry.setPercentage(new BigDecimal(30));

        agreement = new AgreementEntity(dtoEntry);
        agreement.setId(1);
        agreement.setPercentage(new BigDecimal(10));
        agreement.setStatus(StatusAgreement.ATIVO);

        dtoUpdate = new AgreementUpdateDto();
        dtoUpdate.setPercentage(null);
        dtoUpdate.setStatus(StatusAgreement.DESATIVADO);

    }

    @Test
    void shouldCreateAgreement() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        agreementService.registerAgreement(dtoEntry, 1);

        verify(agreementRepository).save(any());
        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldThrowUseNotFound() {

        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> {
            agreementService.registerAgreement(dtoEntry, 1);
        } );
    }

    @Test
    void shouldUpdateAgreement() {

        when(agreementRepository.findById(1)).thenReturn(Optional.of(agreement));

        agreementService.updateAgreement(dtoUpdate, 1);

        verify(agreementRepository).save(argThat(agree ->
                agree.getStatus() == StatusAgreement.DESATIVADO));
    }

    @Test
    void shouldNotUpdateWhenStatusAgreementNull() {

        when(agreementRepository.findById(1)).thenReturn(Optional.of(agreement));

        StatusAgreement originalStatus = agreement.getStatus();

        dtoUpdate.setStatus(null);

        agreementService.updateAgreement(dtoUpdate, 1);

        verify(agreementRepository).save(argThat(agree ->
                agree.getStatus().equals(originalStatus)));
    }

    @Test
    void shouldNotUpdateWhenPercentageNull() {

        when(agreementRepository.findById(1)).thenReturn(Optional.of(agreement));

        BigDecimal originalPercentage = agreement.getPercentage();

        agreementService.updateAgreement(dtoUpdate, 1);

        verify(agreementRepository).save(argThat( agree ->
                agree.getPercentage().equals(originalPercentage)));
    }

    @Test
    void shouldDeleteAgreement() {

        when(agreementRepository.findById(1)).thenReturn(Optional.of(agreement));

        agreementService.deleteAgreement(1);

        verify(agreementRepository).delete(any());
    }

    @Test
    void shouldThrowAgreementNotFound() {

        when(agreementRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AgreementNotFound.class, () -> {
            agreementService.deleteAgreement(1);
        });
    }
}
