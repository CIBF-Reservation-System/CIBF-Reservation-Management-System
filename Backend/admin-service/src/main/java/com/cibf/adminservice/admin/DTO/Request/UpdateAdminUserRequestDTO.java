import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating admin user details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminUserRequestDTO {

    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Phone number can only contain numbers, +, -, (, ), and spaces")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;
}
package com.cibf.adminservice.admin.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

