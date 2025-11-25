package com.cibf.userservice.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserListDTO {
    private UUID userId;
    private String email;
    private String businessName;
    private String contactPerson;
    private String phone;
    private String role;
}

