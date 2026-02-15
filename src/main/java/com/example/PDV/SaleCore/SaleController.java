package com.example.PDV.SaleCore;

import com.example.PDV.SaleCore.SaleDtos.InfoOfProductsSaleDto;
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

    @PostMapping("/make")
    public ResponseEntity<String> makeSale(@RequestBody SaleEntryDto saleEntry) {

        saleService.makeSale(saleEntry);

        return ResponseEntity.status(HttpStatus.CREATED).body("Venda realizada");
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
