package com.example.PDV.SaleCore.Repositories;

import com.example.PDV.SaleCore.Entities.PaymentOfSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOfSaleRepository extends JpaRepository<PaymentOfSaleEntity
        , Integer> {
}
