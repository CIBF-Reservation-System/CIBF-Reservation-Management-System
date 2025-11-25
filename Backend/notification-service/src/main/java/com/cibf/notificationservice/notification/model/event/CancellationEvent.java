package com.cibf.notificationservice.notification.model.event;

import com.google.gson.annotations.SerializedName;
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
public class CancellationEvent {

    @SerializedName("reservation_id")
    private String reservationId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("business_name")
    private String businessName;

    @SerializedName("stalls")
    private List<Stall> stalls;

    @SerializedName("original_reservation_date")
    private LocalDateTime originalReservationDate;

    @SerializedName("cancellation_date")
    private LocalDateTime cancellationDate;

    @SerializedName("total_amount")
    private Double totalAmount;

    @SerializedName("cancellation_reason")
    private String cancellationReason;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Stall {

        @SerializedName("stall_name")
        private String stallName;

        @SerializedName("stall_size")
        private String stallSize;

        @SerializedName("location")
        private String location;

        @SerializedName("price")
        private Double price;
    }
}