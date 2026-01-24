package com.example.PDV.SaleCore.Repositories;

import com.example.PDV.SaleCore.Entities.PaymentOfSaleEntity;
import com.example.PDV.SaleCore.SaleDtos.PaymentSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentOfSaleRepository extends JpaRepository<PaymentOfSaleEntity
        , Integer> {

    @Query("""
           SELECT
                  p.kindOfPayment AS kindOfPayment,
                  SUM(p.totalPaymentSale) AS total
           FROM PaymentOfSaleEntity p
           JOIN p.sale s
           WHERE s.box.id = :boxId
           GROUP BY p.kindOfPayment
           """)
    List<PaymentSummary> summaryOfKind(@Param("boxId") Integer boxId);
}
