package com.example.PDV.BoxCore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends JpaRepository<BoxEntity, Integer> {
}
