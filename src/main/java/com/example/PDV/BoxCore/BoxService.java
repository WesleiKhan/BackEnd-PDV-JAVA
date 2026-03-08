package com.example.PDV.BoxCore;

import com.example.PDV.BoxCore.BoxDtos.BoxEntryDto;
import com.example.PDV.BoxCore.BoxDtos.BoxOpenedOutDto;
import com.example.PDV.BoxCore.BoxEnums.StatusBox;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.OperatorAlreadyBoxOpened;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.SaleCore.Repositories.PaymentOfSaleRepository;
import com.example.PDV.SaleCore.SaleDtos.PaymentSummary;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.EmployeeRepository;
import com.example.PDV.EmployeeCore.EmployeeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoxService {

    private final BoxRepository boxRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeService employeeService;

    private final PaymentOfSaleRepository paymentOfSaleRepository;

    public BoxService(BoxRepository boxRepository,
                      EmployeeRepository employeeRepository,
                      EmployeeService employeeService,
                      PaymentOfSaleRepository paymentOfSaleRepository) {

        this.boxRepository = boxRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.paymentOfSaleRepository = paymentOfSaleRepository;
    }

    public void startBox(BoxEntryDto boxEntry) {

        EmployeeEntity operator = employeeRepository.findById(boxEntry.getIdOperator())
                .orElseThrow(UserNotFound::new);

        if (boxRepository.findByStatus(operator, StatusBox.OPEN).isPresent())
            throw new OperatorAlreadyBoxOpened();

        BoxEntity box = new BoxEntity(operator);

        boxRepository.save(box);
    }

    @Cacheable(
            value = "box_opened",
            key = "'box_opened_employee_' + T(com.example.PDV.EmployeeCore" +
                    ".EmployeeService).currentEmployeeId()"
    )
    public BoxOpenedOutDto BoxOpened() {

        EmployeeEntity operator = employeeService.loggedInEmployee();

        BoxEntity box = boxRepository.findByStatus(operator, StatusBox.OPEN)
                .orElseThrow(BoxNotFound::new);

        List<PaymentSummary> summary =
                paymentOfSaleRepository.summaryOfKind(box.getId());

        Map<KindOfPayment, BigDecimal> payments =
                new HashMap<>();

        for (PaymentSummary r : summary) {
            payments.put(r.getKindOfPayment(), r.getTotal());
        }

        System.out.println(box);

        return new BoxOpenedOutDto(box.getId(), box.getStartDate(),
                box.getOperator().getName(), box.getTotalValue(), payments);
    }

    @CacheEvict(
            value = "box_opened",
            key = "'box_opened_employee_' + T(com.example.PDV.EmployeeCore" +
                    ".EmployeeService).currentEmployeeId()"
    )
    public void finishBox(Integer boxId) {

        BoxEntity newBox = boxRepository.findById(boxId)
                .orElseThrow(BoxNotFound::new);

        newBox.setEndDate(LocalDateTime.now());
        newBox.setStatus_of_box(StatusBox.CLOSE);

        boxRepository.save(newBox);
    }

    @CacheEvict(
            value = "box_opened",
            key = "'box_opened_employee_' + T(com.example.PDV.EmployeeCore" +
                    ".EmployeeService).currentEmployeeId()"
    )
    public void evictCacheBoxOpenedForCurrentUser() {
        // só para evict, nada dentro
    }


}
