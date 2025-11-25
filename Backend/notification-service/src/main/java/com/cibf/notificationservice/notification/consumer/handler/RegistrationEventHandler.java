package com.cibf.notificationservice.notification.consumer.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    //Use ObjectMapper instead of Gson (to match @JsonProperty annotations)
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public void handle(String eventData) {
        try {
            log.info(" Processing registration event...");

            // Parse JSON using Jackson (which understands @JsonProperty)
            RegistrationEvent event = objectMapper.readValue(eventData, RegistrationEvent.class);

            // Validate event
            if (event == null || event.getEmail() == null) {
                log.warn("⚠ Invalid registration event received");
                return;
            }

            log.info("Processing registration for: {} ({})",
                    event.getUserName(), event.getEmail());

            // Process the notification
            notificationService.processRegistrationConfirmation(event);

            log.info(" Registration event processed successfully");

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error(" Invalid JSON format: {}", e.getMessage());
            throw new RuntimeException("Invalid JSON", e);
        } catch (Exception e) {
            log.error(" Error handling registration event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process registration event", e);
        }
    }
}