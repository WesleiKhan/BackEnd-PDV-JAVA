package com.example.PDV.SaleCore;

import com.example.PDV.Exceptions.BusinessException;
import com.example.PDV.Exceptions.DuplicateSaleException;
import com.example.PDV.SaleCore.SaleDtos.SaleEntryDto;
import com.example.PDV.SaleCore.SaleDtos.SyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessSalesService {

    private final SaleService saleService;

    public ProcessSalesService(SaleService saleService) {
        this.saleService = saleService;
    }

    public SyncResult processSales(List<SaleEntryDto> sales) {

        List<SaleEntryDto> processed = new ArrayList<>();
        List<SaleEntryDto> ignoredSale = new ArrayList<>();
        List<SaleEntryDto> failed = new ArrayList<>();
        List<SaleEntryDto> retryable = new ArrayList<>();

        for (SaleEntryDto sale : sales) {

            try {
                saleService.makeSale(sale);
                processed.add(sale);

            }catch (DuplicateSaleException e){
                ignoredSale.add(sale);

            }catch (BusinessException e){
                failed.add(sale);

            }catch (Exception e){
                retryable.add(sale);

            }
        }

        return new SyncResult(processed, ignoredSale, failed, retryable);

    }
}
