package com.cibf.notificationservice.notification.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationEvent {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("phone")
    private String phoneNumber;

    @JsonProperty("registration_date")
    private LocalDateTime registrationDate;

    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("business_type")
    private String businessType;

    @JsonProperty("temporary_password")
    private String temporaryPassword;
}