package com.example.PDV.Agreement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementRepository extends JpaRepository<AgreementEntity,
        Integer> {
}
