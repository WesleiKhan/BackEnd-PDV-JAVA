package com.example.PDV.SaleCore.Repositories;

import com.example.PDV.SaleCore.Entities.ItemsForSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsForSaleRepository extends JpaRepository<ItemsForSaleEntity, Integer> {
}
