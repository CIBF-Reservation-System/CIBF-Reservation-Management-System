package com.cibf.notificationservice.notification.consumer.handler;

import com.google.gson.Gson;
import com.cibf.notificationservice.notification.model.event.RegistrationEvent;
import com.cibf.notificationservice.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationEventHandler {

    private final NotificationService notificationService;
    private final Gson gson;

    public void handle(String eventData) {
        try {
            log.info(" Processing registration event...");

            // Parse JSON to RegistrationEvent (java object)
            RegistrationEvent event = gson.fromJson(eventData, RegistrationEvent.class);

            // Validate event
            if (event == null || event.getEmail() == null) {
                log.warn(" Invalid registration event received");
                return;
            }

            log.info("Processing registration for: {} ({})",
                    event.getUserName(), event.getEmail());

            // Process the notification
            notificationService.processRegistrationConfirmation(event);

            log.info(" Registration event processed successfully");

        } catch (Exception e) {
            log.error(" Error handling registration event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process registration event", e);
        }
    }
}