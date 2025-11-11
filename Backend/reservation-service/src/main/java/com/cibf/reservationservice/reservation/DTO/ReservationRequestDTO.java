package com.cibf.reservationservice.reservation.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    private UUID userId;
    private UUID stallId;
    private String businessName;
    private String email;
    private String phoneNumber;
}