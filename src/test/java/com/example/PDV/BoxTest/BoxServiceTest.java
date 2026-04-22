package com.example.PDV.BoxTest;

import com.example.PDV.BoxCore.BoxDtos.BoxEntryDto;
import com.example.PDV.BoxCore.BoxDtos.BoxOpenedOutDto;
import com.example.PDV.BoxCore.BoxEntity;
import com.example.PDV.BoxCore.BoxEnums.StatusBox;
import com.example.PDV.BoxCore.BoxRepository;
import com.example.PDV.BoxCore.BoxService;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.EmployeeRepository;
import com.example.PDV.EmployeeCore.EmployeeService;
import com.example.PDV.EmployeeCore.Enums.Roles;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.OperatorAlreadyBoxOpened;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.SaleCore.Repositories.PaymentOfSaleRepository;
import com.example.PDV.SaleCore.SaleDtos.PaymentSummary;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BoxServiceTest {

    @Mock
    BoxRepository boxRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmployeeService employeeService;

    @Mock
    PaymentOfSaleRepository paymentOfSaleRepository;

    @Mock
    ActivityLogsService activityLogsService;

    @InjectMocks
    BoxService boxService;

    private BoxEntryDto dto;

    private BoxEntity box;

    private BoxOpenedOutDto boxOpenedOutDto;

    private EmployeeEntryDto dtoOperator;

    private EmployeeEntity operator;

    private List<PaymentSummary> summaries;

    @BeforeEach
    void setUp() {

        dto = new BoxEntryDto();
        dto.setIdOperator(3);

        dtoOperator =new EmployeeEntryDto();
        dtoOperator.setName("Boneco test");
        dtoOperator.setPassword("123");
        dtoOperator.setRole(Roles.ROLE_OPERATOR_BOX);

        operator = new EmployeeEntity(dtoOperator);
        operator.setId(3);

        Map<KindOfPayment, BigDecimal> kindPayment = new HashMap<>();
        kindPayment.put(KindOfPayment.DINHEIRO, new BigDecimal(50));

        LocalDateTime data = LocalDateTime.of(2026, 4, 22, 15, 30, 45);

        box = new BoxEntity(operator);
        box.setId(1);
        box.setStatus_of_box(StatusBox.OPEN);
        box.setTotalValue(new BigDecimal(200));
        box.setStartDate(data);

        boxOpenedOutDto = new BoxOpenedOutDto(box.getId(), data,
                operator.getName(), box.getTotalValue(), kindPayment);
    }

    @Test
    void shouldStartBox() {

        when(employeeRepository.findById(3)).thenReturn(Optional.of(operator));

        when(boxRepository.findByStatus(operator, StatusBox.OPEN)).thenReturn(Optional.empty());

        boxService.startBox(dto);

        verify(boxRepository).save(any());
        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldThrowUserNotFound() {

        when(employeeRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> {
            boxService.startBox(dto);
        });
    }

    @Test
    void shouldThrowOperatorAlreadyBoxOpened() {

        when(employeeRepository.findById(3)).thenReturn(Optional.of(operator));

        when(boxRepository.findByStatus(operator, StatusBox.OPEN)).thenReturn(Optional.of(box));

        assertThrows(OperatorAlreadyBoxOpened.class, () -> {
            boxService.startBox(dto);
        });
    }

    @Test
    void shouldBoxOpened() {

        PaymentSummary summary = Mockito.mock(PaymentSummary.class);

        when(summary.getKindOfPayment())
                .thenReturn(KindOfPayment.DINHEIRO);

        when(summary.getTotal())
                .thenReturn(new BigDecimal(50));

        List<PaymentSummary> summaries = List.of(summary);

        when(employeeService.loggedInEmployee()).thenReturn(operator);

        when(boxRepository.findByStatus(operator, StatusBox.OPEN))
                .thenReturn(Optional.of(box));

        when(paymentOfSaleRepository.summaryOfKind(1))
                .thenReturn(summaries);

        BoxOpenedOutDto result = boxService.BoxOpened();

        assertEquals(box.getId(), result.id());
        assertEquals(operator.getName(), result.operator());
        assertEquals(box.getTotalValue(), result.totalValue());

        assertEquals(new BigDecimal(50),
                result.payments().get(KindOfPayment.DINHEIRO));
    }

    @Test
    void shouldFinishBox() {

        when(boxRepository.findById(1)).thenReturn(Optional.of(box));

        boxService.finishBox(1);

        assertEquals(StatusBox.CLOSE, box.getStatus_of_box());
        assertNotNull(box.getEndDate());

        verify(boxRepository).save(any());
        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldThrowBoxNotFound() {

        when(boxRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BoxNotFound.class, () -> {
            boxService.finishBox(1);
        });

    }

}
