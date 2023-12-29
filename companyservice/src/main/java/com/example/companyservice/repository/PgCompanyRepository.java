package com.example.companyservice.repository;

import com.example.companyservice.entity.PgCompany;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PgCompanyRepository extends JpaRepository<PgCompany, Long> {
    PgCompany getPgCompanyById(Long id);
}
