package com.example.PDV.Agreement;

import com.example.PDV.Agreement.Dtos.AgreementEntryDto;
import com.example.PDV.Agreement.Dtos.AgreementOutDto;
import com.example.PDV.Agreement.Dtos.AgreementUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agreement")
public class AgreementController {

    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {

        this.agreementService = agreementService;
    }

    @PostMapping("/register/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> registerAgreement(@PathVariable Integer id
            , @RequestBody AgreementEntryDto entry) {

        agreementService.registerAgreement(entry, id);

        return ResponseEntity.ok().body("Convenio Registrado com sucesso");
    }

    @GetMapping("/agreements")
    public ResponseEntity<List<AgreementOutDto>> getAgreements() {

        return ResponseEntity.ok().body(agreementService.getAgreements());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> updateAgreement(@PathVariable Integer id,
                                                 @RequestBody AgreementUpdateDto entry) {

        agreementService.updateAgreement(entry,id);

        return ResponseEntity.ok().body("Convenio Atualizado com sucesso");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> deleteAgreement(@PathVariable Integer id) {

        agreementService.deleteAgreement(id);

        return ResponseEntity.ok().body("Convenio deletado com sucesso");
    }
}
