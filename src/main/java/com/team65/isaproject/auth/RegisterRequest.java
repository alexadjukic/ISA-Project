package com.team65.isaproject.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private Integer addressId;
    private String phoneNumber;
    private String profession;
    private Integer companyId;
}
