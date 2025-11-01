package com.cibf.userservice.user_service.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String message;
    private String error;
    private LoginUserDTO user;

    // not visible in the JSON, only usable by the controller.
    @JsonIgnore
    private String token;
}
