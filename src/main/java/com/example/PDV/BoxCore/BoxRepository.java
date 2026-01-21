package com.example.PDV.BoxCore;

import com.example.PDV.BoxCore.BoxEnums.StatusBox;
import com.example.PDV.UsersCore.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<BoxEntity, Integer> {

    @Query("""
                SELECT b 
                FROM BoxEntity b 
                WHERE b.operator = :operator 
                AND b.status_of_box = :status
            """)
    Optional<BoxEntity> findByStatus(
         @Param("operator") UserEntity operator ,
         @Param("status")  StatusBox status
    );
}
