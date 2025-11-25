package com.cibf.notificationservice.notification.consumer.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.cibf.notificationservice.notification.model.event.CancellationEvent;
import com.cibf.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancellationEventHandler {

    private final NotificationService notificationService;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                            LocalDateTime.parse(json.getAsString(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
            .create();

    public void handle(String eventData) {

        log.info(" Processing CANCELLATION event");

        // First check if eventData is null or empty
        if (eventData == null) {
            log.error(" Event data is NULL - no data received from Kafka");
            throw new IllegalArgumentException("Event data is null - no data received from Kafka");
        }

        if (eventData.trim().isEmpty()) {
            log.error(" Event data is EMPTY - received empty string from Kafka");
            throw new IllegalArgumentException("Event data is empty - received empty string from Kafka");
        }

        //  Log the raw incoming JSON first
        log.info(" Raw event data received (length: {}):", eventData.length());
        log.info("{}", eventData);

        try {
            // Parse JSON to CancellationEvent with explicit error handling
            CancellationEvent event = null;
            try {
                event = gson.fromJson(eventData, CancellationEvent.class);
            } catch (Exception parseEx) {
                log.error(" Gson parsing exception: {}", parseEx.getMessage());
                log.error("Full parse exception:", parseEx);
                throw new IllegalArgumentException(
                        "Failed to parse JSON with Gson: " + parseEx.getMessage() +
                                ". JSON: " + eventData,
                        parseEx
                );
            }

            //  Better null check with more info
            if (event == null) {
                throw new IllegalArgumentException(
                        "Failed to parse cancellation event - Gson returned null. " +
                                "This usually means the JSON is empty, invalid, or field names don't match. " +
                                "Received JSON: " + eventData
                );
            }

            log.info(" JSON parsed successfully");

            // Validate event
            validateEvent(event);

            // Log event details
            logEventDetails(event);

            // Process cancellation notification
            notificationService.processCancellationNotification(event);

            log.info(" Cancellation event processed successfully");
            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        } catch (Exception e) {
            log.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.error(" Error handling cancellation event");
            log.error("Error type: {}", e.getClass().getName());
            log.error("Error message: {}", e.getMessage());
            log.error("Event data that failed: {}", eventData);
            log.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.error("Full stack trace:", e);
            throw new RuntimeException("Failed to handle cancellation event", e);
        }
    }

    private void validateEvent(CancellationEvent event) {

        log.info(" Validating cancellation event...");

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