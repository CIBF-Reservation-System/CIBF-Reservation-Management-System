package lk.bookfair.notification.consumer.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lk.bookfair.notification.model.event.ReservationEvent;
import lk.bookfair.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventHandler {

    private final NotificationService notificationService;


    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public void handle(String eventData) {

        log.info(" Processing reservation event...");

        try {
            ReservationEvent event = objectMapper.readValue(eventData, ReservationEvent.class);

            if (event == null) {
                log.warn(" Received null event");
                return;
            }

            if (event.getUserEmail() == null || event.getUserEmail().isEmpty()) {
                log.warn(" Event missing email address");
                return;
            }

            log.info(" Event Details:");
            log.info("   Reservation ID: {}", event.getReservationId());
            log.info("   User: {} ({})", event.getUserName(), event.getUserEmail());
            log.info("   Business: {}", event.getBusinessName());
            log.info("   Stalls: {}", event.getStalls().size());
            log.info("   Total: Rs. {}", event.getTotalAmount());

            notificationService.processReservationConfirmation(event);

            log.info(" Reservation event processed successfully");

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error(" Invalid JSON format: {}", e.getMessage());
            throw new RuntimeException("Invalid JSON", e);
        } catch (Exception e) {
            log.error(" Error handling reservation event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process reservation event", e);
        }
    }
}