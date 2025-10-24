package com.cibf.userservice.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    private String email;
    private String password;
    private String businessName;
    private String contactPerson;
    private String phone;
    private String roleName;
}
