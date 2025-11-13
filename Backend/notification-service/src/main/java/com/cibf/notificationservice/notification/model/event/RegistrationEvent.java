package com.cibf.notificationservice.notification.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationEvent {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("business_type")
    private String businessType;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("registration_date")
    private LocalDateTime registrationDate;

    @JsonProperty("temporary_password")
    private String temporaryPassword;
}