package com.example.PDV.SaleCore;

import com.example.PDV.SaleCore.SaleDtos.SaleEntryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {

        this.saleService = saleService;
    }

    @PostMapping("/make/{id}")
    public ResponseEntity<String> makeSale(@RequestBody SaleEntryDto saleEntry,
                                             @PathVariable Integer id) {

        saleEntry.setBoxId(id);

        saleService.makeSale(saleEntry);

        return ResponseEntity.status(HttpStatus.CREATED).body("Venda realizada");
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelSale(@PathVariable Integer id) {

        saleService.cancelSele(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
