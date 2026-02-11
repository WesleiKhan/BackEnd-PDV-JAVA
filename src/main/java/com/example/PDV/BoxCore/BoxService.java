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
import com.example.PDV.UsersCore.UserEntity;
import com.example.PDV.UsersCore.UserRepository;
import com.example.PDV.UsersCore.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class BoxService {

    private final BoxRepository boxRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final PaymentOfSaleRepository paymentOfSaleRepository;

    public BoxService(BoxRepository boxRepository,
                      UserRepository userRepository,
                      UserService userService,
                      PaymentOfSaleRepository paymentOfSaleRepository) {

        this.boxRepository = boxRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.paymentOfSaleRepository = paymentOfSaleRepository;
    }

    public void startBox(BoxEntryDto boxEntry) {

        UserEntity operator = userRepository.findById(boxEntry.getIdOperator())
                .orElseThrow(UserNotFound::new);

        if (boxRepository.findByStatus(operator, StatusBox.OPEN).isPresent())
            throw new OperatorAlreadyBoxOpened();

        BoxEntity box = new BoxEntity(operator);

        boxRepository.save(box);
    }

    @Cacheable(
            value = "box_opened",
            key = "'box_opened_user_' + T(com.example.PDV.UsersCore.UserService).currentUserId()"
    )
    public BoxOpenedOutDto BoxOpened() {

        UserEntity operator = userService.loggedInUser();

        BoxEntity box = boxRepository.findByStatus(operator, StatusBox.OPEN)
                .orElseThrow(BoxNotFound::new);

        List<PaymentSummary> summary =
                paymentOfSaleRepository.summaryOfKind(box.getId());

        Map<KindOfPayment, BigDecimal> payments =
                new EnumMap<>(KindOfPayment.class);

        for (PaymentSummary r : summary) {
            payments.put(r.getKindOfPayment(), r.getTotal());
        }

        System.out.println(box);

        return new BoxOpenedOutDto(box.getId(), box.getStartDate(),
                box.getOperator().getName(), box.getTotalValue(), payments);
    }

    @CacheEvict(
            value = "box_opened",
            key = "'box_opened_user_' + T(com.example.PDV.UsersCore.UserService).currentUserId()"
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
            key = "'box_opened_user_' + T(com.example.PDV.UsersCore.UserService).currentUserId()"
    )
    public void evictCacheBoxOpenedForCurrentUser() {
        // só para evict, nada dentro
    }


}
