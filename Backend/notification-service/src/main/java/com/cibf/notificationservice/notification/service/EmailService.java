package lk.bookfair.notification.service;

import jakarta.mail.internet.MimeMessage;
import lk.bookfair.notification.model.event.ReservationEvent;
import lk.bookfair.notification.model.event.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;


      //Send reservation confirmation email with QR code
    public void sendReservationConfirmation(ReservationEvent event, String qrCodeBase64) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(event.getUserEmail());
            helper.setSubject(" Stall Reservation Confirmation - Colombo International Bookfair");

            // Create email context
            Context context = new Context();
            context.setVariable("userName", event.getUserName());
            context.setVariable("businessName", event.getBusinessName());
            context.setVariable("reservationId", event.getReservationId());
            context.setVariable("stalls", event.getStalls());
            context.setVariable("reservationDate", event.getReservationDate());
            context.setVariable("qrCode", "data:image/png;base64," + qrCodeBase64);

            String htmlContent = templateEngine.process("reservation-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Reservation confirmation email sent to: {}", event.getUserEmail());

        } catch (Exception e) {
            log.error("Failed to send reservation confirmation email: {}", e.getMessage(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

     //Send registration confirmation email
    public void sendRegistrationConfirmation(RegistrationEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(event.getEmail());
            helper.setSubject("🎉 Welcome to Colombo International Bookfair");

            Context context = new Context();
            context.setVariable("userName", event.getUserName());
            context.setVariable("businessName", event.getBusinessName());
            context.setVariable("email", event.getEmail());
            context.setVariable("temporaryPassword", event.getTemporaryPassword());
            context.setVariable("registrationDate", event.getRegistrationDate());

            String htmlContent = templateEngine.process("registration-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Registration confirmation email sent to: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Failed to send registration confirmation email: {}", e.getMessage(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    //Send password reset email
    public void sendPasswordResetEmail(String email, String userName, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(email);
            helper.setSubject(" Password Reset Request");

            String resetLink = "https://bookfair.lk/reset-password?token=" + resetToken;

            String htmlContent = String.format("""
                <html>
                <body style='font-family: Arial, sans-serif;'>
                    <h2>Password Reset Request</h2>
                    <p>Hi %s,</p>
                    <p>You requested to reset your password. Click the link below:</p>
                    <a href='%s' style='background-color: #4CAF50; color: white; padding: 10px 20px; 
                       text-decoration: none; border-radius: 5px;'>Reset Password</a>
                    <p>This link expires in 1 hour.</p>
                    <p>If you didn't request this, please ignore this email.</p>
                </body>
                </html>
                """, userName, resetLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            log.info("Password reset email sent to: {}", email);

        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }
}