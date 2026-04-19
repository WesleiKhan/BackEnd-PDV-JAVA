package com.example.PDV.SaleCore;

import com.example.PDV.SaleCore.SaleDtos.InfoOfProductsSaleDto;
import com.example.PDV.SaleCore.SaleDtos.SaleEntryDto;
import com.example.PDV.SaleCore.SaleDtos.SyncResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;
    private final ProcessSalesService processSalesService;

    public SaleController(SaleService saleService,
                          ProcessSalesService processSalesService) {

        this.saleService = saleService;
        this.processSalesService = processSalesService;
    }

    @PostMapping("/make")
    public ResponseEntity<String> makeSale(@RequestBody SaleEntryDto saleEntry) {

        saleService.makeSale(saleEntry);
        saleService.evictCacheInfosInDataRedisOfProductsSale();

        return ResponseEntity.status(HttpStatus.CREATED).body("Venda realizada");
    }

    @PostMapping("/sync/process")
    public ResponseEntity<SyncResult> syncProcessSale(@RequestBody List<SaleEntryDto> sales) {

        SyncResult result = processSalesService.processSales(sales);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelSale(@PathVariable Integer id) {

        saleService.cancelSele(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/info/products")
    public ResponseEntity<String> toWriteProductsInRedis(@RequestBody InfoOfProductsSaleDto info) {

        InfoOfProductsSaleDto infoProducts =
                saleService.toWriteInfosInDataRedisOfProductsSale(info);

        return ResponseEntity.status(HttpStatus.CREATED).body("Objeto " +
                "Adicionado no Redis");
    }

    @GetMapping("/info/products")
    public ResponseEntity<InfoOfProductsSaleDto> getInfoProductsOfSaleFromRedis() {

        InfoOfProductsSaleDto info =
                saleService.readInfosInDataRedisOfProductsSale();

        return ResponseEntity.status(HttpStatus.OK).body(info);
    }

    @DeleteMapping("/info/products")
    public ResponseEntity<String> deleteProductsInRedis() {

        saleService.evictCacheInfosInDataRedisOfProductsSale();

        return ResponseEntity.status(HttpStatus.CREATED).body("Objeto " +
                "Deletado no Redis");
    }
}
