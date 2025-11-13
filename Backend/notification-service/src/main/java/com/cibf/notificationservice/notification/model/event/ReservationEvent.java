package com.cibf.notificationservice.notification.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEvent {

    @JsonProperty("reservation_id")
    private String reservationId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("stalls")
    private List<StallInfo> stalls;

    @JsonProperty("reservation_date")
    private LocalDateTime reservationDate;

    @JsonProperty("total_amount")
    private Double totalAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StallInfo {
        @JsonProperty("stall_id")
        private String stallId;

        @JsonProperty("stall_name")
        private String stallName;

        @JsonProperty("stall_size")
        private String stallSize;

        @JsonProperty("location")
        private String location;

        @JsonProperty("price")
        private Double price;
    }
}