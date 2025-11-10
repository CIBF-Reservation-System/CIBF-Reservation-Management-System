package lk.bookfair.notification.consumer;

import lk.bookfair.notification.consumer.handler.RegistrationEventHandler;
import lk.bookfair.notification.consumer.handler.ReservationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final ReservationEventHandler reservationEventHandler;
    private final RegistrationEventHandler registrationEventHandler;

    @KafkaListener(
            topics = "${app.kafka.topics.reservation}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReservationEvents(

            // 1. @Payload = The actual message content (JSON string)
            @Payload String message,

            // 2. Topic name (for logging)
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,

            // 3. Partition number (0, 1, or 2)
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,

            // 4. Offset (message position in partition)
            @Header(KafkaHeaders.OFFSET) long offset,

            // 5. Timestamp (when message was created)
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,

            // 6. Acknowledgment (to mark message as read)
            Acknowledgment acknowledgment
    ) {

        log.info(" RECEIVED MESSAGE FROM KAFKA");
        log.info(" Topic:     {}", topic);
        log.info(" Partition: {}", partition);
        log.info(" Offset:    {}", offset);
        log.info(" Timestamp: {}", new java.util.Date(timestamp));
        log.info(" Message:   {}", message);

        try {
            reservationEventHandler.handle(message);  //pass to the handler

            acknowledgment.acknowledge();

            log.info(" Message processed and acknowledged");
            log.info("   Offset {} marked as read", offset);

        } catch (Exception e) {

            log.error(" ERROR processing message at offset {}", offset);
            log.error("   Error: {}", e.getMessage(), e);
            log.error("   Message will NOT be acknowledged");
            log.error("   Next consumer restart will retry this message");

        }
    }

    @KafkaListener(
            topics = "${app.kafka.topics.registration}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeRegistrationEvents(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment
    ) {

        log.info(" RECEIVED REGISTRATION EVENT");
        log.info("Topic: {}, Partition: {}, Offset: {}", topic, partition, offset);

        try {
            registrationEventHandler.handle(message);
            acknowledgment.acknowledge();
            log.info(" Registration event processed successfully");

        } catch (Exception e) {
            log.error("Error processing registration event: {}", e.getMessage(), e);
        }
    }
}