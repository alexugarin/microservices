package com.example.userservice.repository;

import com.example.userservice.entity.PgUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PgUserRepository extends JpaRepository<PgUser, Long> {
    PgUser getPgUserById(Long Id);
    List<PgUser> getUsersByCompanyId(Long id);
}
