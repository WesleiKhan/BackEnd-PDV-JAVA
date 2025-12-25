package com.example.PDV.UsersCore;

import com.example.PDV.UsersCore.UserDtos.UserOutDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserOutDto> findAllBy();

    Optional<UserEntity> findByName(String name);
}
