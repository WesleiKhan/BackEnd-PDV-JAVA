package com.example.PDV.Agreement;

import com.example.PDV.Agreement.Dtos.AgreementEntryDto;
import com.example.PDV.Agreement.Dtos.AgreementOutDto;
import com.example.PDV.Agreement.Dtos.AgreementUpdateDto;
import com.example.PDV.CustomerCore.Dtos.InfosCustomerDto;
import com.example.PDV.Agreement.Exceptions.AgreementNotFound;
import com.example.PDV.CustomerCore.CustomerEntity;
import com.example.PDV.CustomerCore.CustomerRepository;
import com.example.PDV.Exceptions.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgreementService {

    private final AgreementRepository agreementRepository;

    private final CustomerRepository customerRepository;

    public AgreementService(AgreementRepository agreementRepository,
                            CustomerRepository customerRepository) {

        this.agreementRepository = agreementRepository;
        this.customerRepository = customerRepository;
    }

    public void registerAgreement(AgreementEntryDto entry, Integer id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Cliente não foi " +
                "Encontrado"));

        AgreementEntity newAgreement = new AgreementEntity(entry);
        newAgreement.setCustomer(customer);


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

        agreementRepository.save(agreement);
    }

    public void deleteAgreement(Integer id) {

        AgreementEntity agreement = agreementRepository.findById(id)
                .orElseThrow(AgreementNotFound::new);

        agreementRepository.delete(agreement);
    }
}
