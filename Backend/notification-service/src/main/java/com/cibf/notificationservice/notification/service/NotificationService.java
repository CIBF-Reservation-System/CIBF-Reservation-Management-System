package lk.bookfair.notification.service;

import lk.bookfair.notification.model.entity.NotificationLog;
import lk.bookfair.notification.model.enums.NotificationType;
import lk.bookfair.notification.model.event.RegistrationEvent;
import lk.bookfair.notification.model.event.ReservationEvent;
import lk.bookfair.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final QRCodeService qrCodeService;
    private final NotificationLogRepository notificationLogRepository;


    @Transactional
    public void processReservationConfirmation(ReservationEvent event) {

        log.info(" Processing reservation notification for: {}", event.getReservationId());


        NotificationLog notificationLog = NotificationLog.builder()
                .recipientEmail(event.getUserEmail())
                .recipientName(event.getUserName())
                .notificationType(NotificationType.RESERVATION_CONFIRMATION)
                .subject("Stall Reservation Confirmation")
                .referenceId(event.getReservationId())
                .status("PENDING")
                .build();


        notificationLog = notificationLogRepository.save(notificationLog); //save to MySQL

        log.info(" Notification log created with ID: {}", notificationLog.getId());

        try {

            String qrData = qrCodeService.generateReservationQRData(
                    event.getReservationId(),
                    event.getUserId(),
                    event.getUserName()
            );
            String qrCodeBase64 = qrCodeService.generateQRCodeBase64(qrData);

            log.info(" QR code generated");

            emailService.sendReservationConfirmation(event, qrCodeBase64);

            log.info(" Email sent successfully");


            notificationLog.setStatus("SENT");
            notificationLog.setSentAt(LocalDateTime.now());
            notificationLog.setMessage("Reservation confirmation sent with QR code");

            notificationLogRepository.save(notificationLog);

            log.info(" Notification log updated to SENT");

        } catch (Exception e) {

            log.error(" Failed to process notification: {}", e.getMessage(), e);

            notificationLog.setStatus("FAILED");
            notificationLog.setErrorMessage(e.getMessage());

            notificationLogRepository.save(notificationLog);

            log.error(" Notification log updated to FAILED");
        }
    }

    @Transactional
    public void processRegistrationConfirmation(RegistrationEvent event) {

        log.info(" Processing registration notification for: {}", event.getEmail());

        NotificationLog notificationLog = NotificationLog.builder()
                .recipientEmail(event.getEmail())
                .recipientName(event.getUserName())
                .notificationType(NotificationType.REGISTRATION_CONFIRMATION)
                .subject("Welcome to Colombo Bookfair")
                .referenceId(event.getUserId())
                .status("PENDING")
                .build();

        notificationLog = notificationLogRepository.save(notificationLog);

        try {
            emailService.sendRegistrationConfirmation(event);

            notificationLog.setStatus("SENT");
            notificationLog.setSentAt(LocalDateTime.now());
            notificationLog.setMessage("Registration confirmation sent");

            notificationLogRepository.save(notificationLog);

            log.info(" Registration notification processed successfully");

        } catch (Exception e) {
            log.error(" Failed to process registration notification: {}", e.getMessage(), e);

            notificationLog.setStatus("FAILED");
            notificationLog.setErrorMessage(e.getMessage());
            notificationLogRepository.save(notificationLog);
        }
    }
}