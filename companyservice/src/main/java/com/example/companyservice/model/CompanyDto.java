package com.example.companyservice.model;

import com.example.companyservice.entity.PgCompany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String ogrn;
    private String description;
    private String directorFio;
    private Long directorId;

    public PgCompany toEntity(){
        var pgUser = new PgCompany();
        pgUser.setName(this.getName());
        pgUser.setOgrn(this.getOgrn());
        pgUser.setDescription(this.getDescription());
        pgUser.setDirectorId(this.getDirectorId());
        return pgUser;
    }
}
