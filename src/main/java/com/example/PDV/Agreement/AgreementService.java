package com.example.PDV.Agreement;

import com.example.PDV.Agreement.Dtos.AgreementEntryDto;
import com.example.PDV.Agreement.Dtos.AgreementOutDto;
import com.example.PDV.Agreement.Dtos.AgreementUpdateDto;
import com.example.PDV.CustomerCore.Dtos.InfosCustomerDto;
import com.example.PDV.Agreement.Exceptions.AgreementNotFound;
import com.example.PDV.CustomerCore.CustomerEntity;
import com.example.PDV.CustomerCore.CustomerRepository;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgreementService {

    private final AgreementRepository agreementRepository;

    private final CustomerRepository customerRepository;

    private final ActivityLogsService activityLogsService;

    public AgreementService(AgreementRepository agreementRepository,
                            CustomerRepository customerRepository,
                            ActivityLogsService activityLogsService) {

        this.agreementRepository = agreementRepository;
        this.customerRepository = customerRepository;
        this.activityLogsService = activityLogsService;
    }

    public void registerAgreement(AgreementEntryDto entry, Integer id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Cliente não foi " +
                "Encontrado"));

        AgreementEntity newAgreement = new AgreementEntity(entry);
        newAgreement.setCustomer(customer);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.AGREEMENT,
                newAgreement.getId(), TypeAction.CREATE));

        agreementRepository.save(newAgreement);
    }

    public List<AgreementOutDto> getAgreements() {

        return agreementRepository.findAll()
                .stream().map(a -> new AgreementOutDto(
                        a.getId(),
                        a.getCustomer().seeInfosCustomer(),
                        a.getDateStartAgreement(),
                        a.getDateEndAgreement(),
                        a.getStatus(),
                        a.getPercentage()
                )).toList();
    }

    public void updateAgreement(AgreementUpdateDto entryUpdate, Integer id) {

        AgreementEntity agreement = agreementRepository.findById(id)
                .orElseThrow(AgreementNotFound::new);

        agreement.updateAgreement(entryUpdate);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.AGREEMENT,
                agreement.getId(), TypeAction.UPDATE));

        agreementRepository.save(agreement);
    }

    public void deleteAgreement(Integer id) {

        AgreementEntity agreement = agreementRepository.findById(id)
                .orElseThrow(AgreementNotFound::new);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.AGREEMENT,
                agreement.getId(), TypeAction.DELETE));

        agreementRepository.delete(agreement);
    }

    @CachePut(
            value = "Agreement_Out",
            key = "'Agreement_Out_id_' + T(com.example.PDV" +
                    ".EmployeeCore.EmployeeService).currentEmployeeId()"
    )
    public AgreementOutDto saveAgreementOnRedis(AgreementOutDto entry) {
        return entry;
    }

    @Cacheable(
            value = "Agreement_Out",
            key = "'Agreement_Out_id_' + T(com.example.PDV" +
                    ".EmployeeCore.EmployeeService).currentEmployeeId()",
            unless = "#result == null"
    )
    public AgreementOutDto readAgreementOnRedis() {
        return null;
    }

    @CacheEvict(
            value = "Agreement_Out",
            key = "'Agreement_Out_id_' + T(com.example.PDV" +
                    ".EmployeeCore.EmployeeService).currentEmployeeId()"
    )
    public void deleteAgreementOnRedis() {

    }
}
