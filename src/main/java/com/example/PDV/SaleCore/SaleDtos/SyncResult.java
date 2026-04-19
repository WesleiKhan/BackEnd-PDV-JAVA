package com.example.PDV.SaleCore.SaleDtos;

import java.util.List;

public record SyncResult(List<SaleEntryDto> processed,
                         List<SaleEntryDto> ignored,
                         List<SaleEntryDto> failed,
                         List<SaleEntryDto> retryable) {
}
