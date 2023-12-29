package com.example.userservice.model;

import com.example.userservice.entity.PgUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String companyName;
    private Long companyId;
    private String password;
    private boolean enabled;

    public PgUser toEntity(){
        PgUser pgUser = new PgUser();
        pgUser.setName(this.getName());
        pgUser.setEmail(this.getEmail());
        pgUser.setLogin(this.getLogin());
        pgUser.setCompanyId(this.getCompanyId());
        pgUser.setEnabled(this.isEnabled());
        pgUser.setPassword(this.getPassword());
        return pgUser;
    }
}
