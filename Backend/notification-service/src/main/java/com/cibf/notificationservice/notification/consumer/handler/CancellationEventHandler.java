package com.cibf.notificationservice.notification.consumer.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.cibf.notificationservice.notification.model.event.CancellationEvent;
import com.cibf.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancellationEventHandler {

    private final NotificationService notificationService;
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    public void handle(String eventData) {

        log.info(" Processing CANCELLATION event");

        try {
            // Parse JSON to CancellationEvent
            CancellationEvent event = gson.fromJson(eventData, CancellationEvent.class);

            // Validate event
            validateEvent(event);

            // Log event details
            logEventDetails(event);

            // Process cancellation notification
            notificationService.processCancellationNotification(event);

            log.info(" Cancellation event processed successfully");

        } catch (Exception e) {
            log.error(" Error handling cancellation event", e);
            log.error("Event data: {}", eventData);
            throw new RuntimeException("Failed to handle cancellation event", e);
        }
    }

    private void validateEvent(CancellationEvent event) {

        if (event == null) {
            throw new IllegalArgumentException("Cancellation event is null");
        }

        if (event.getReservationId() == null || event.getReservationId().isEmpty()) {
            throw new IllegalArgumentException("Reservation ID is required");
        }

        if (event.getUserEmail() == null || event.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("User email is required");
        }

        if (!event.getUserEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format: " + event.getUserEmail());
        }

        if (event.getStalls() == null || event.getStalls().isEmpty()) {
            throw new IllegalArgumentException("Stalls information is required");
        }

        log.info(" Event validation passed");
    }

    private void logEventDetails(CancellationEvent event) {

        log.info(" Cancellation Details:");
        log.info("   Reservation ID: {}", event.getReservationId());
        log.info("   User ID: {}", event.getUserId());
        log.info("   User Name: {}", event.getUserName());
        log.info("   User Email: {}", event.getUserEmail());
        log.info("   Business Name: {}", event.getBusinessName());
        log.info("   Number of Stalls: {}", event.getStalls().size());
        log.info("   Total Amount: LKR {}", event.getTotalAmount());
        log.info("   Original Reservation Date: {}", event.getOriginalReservationDate());
        log.info("   Cancellation Date: {}", event.getCancellationDate());

        if (event.getCancellationReason() != null) {
            log.info("   Cancellation Reason: {}", event.getCancellationReason());
        }

        log.info("   Cancelled Stalls:");
        event.getStalls().forEach(stall ->
                log.info("      • {} ({}) - {} - LKR {}",
                        stall.getStallName(),
                        stall.getStallSize(),
                        stall.getLocation(),
                        stall.getPrice())
        );
    }
}