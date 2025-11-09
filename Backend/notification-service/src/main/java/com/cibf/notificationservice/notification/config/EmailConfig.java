package lk.bookfair.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth:false}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:false}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required:false}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout:5000}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout:5000}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout:5000}")
    private int writeTimeout;

    @Bean
    public JavaMailSender javaMailSender() {

        // Create implementation
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        // Set credentials (if provided)
        if (mailUsername != null && !mailUsername.isEmpty()) {
            mailSender.setUsername(mailUsername);
        }

        if (mailPassword != null && !mailPassword.isEmpty()) {
            mailSender.setPassword(mailPassword);
        }

        Properties props = mailSender.getJavaMailProperties();

        // Transport protocol
        props.put("mail.transport.protocol", "smtp");

        props.put("mail.smtp.auth", smtpAuth);

        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.starttls.required", starttlsRequired);

        // Timeouts (prevent hanging)
        props.put("mail.smtp.connectiontimeout", connectionTimeout);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.writetimeout", writeTimeout);

        // Debug mode (set to true to see SMTP conversation)
        props.put("mail.debug", "false");

        return mailSender;
    }