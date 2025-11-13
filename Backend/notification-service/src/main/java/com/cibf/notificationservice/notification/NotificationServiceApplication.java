package com.cibf.notificationservice.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@Slf4j
public class NotificationServiceApplication {

	public static void main(String[] args) {
		// Get reference to main thread
		final Thread mainThread = Thread.currentThread();

		// Add shutdown hook for graceful shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info(" Detected shutdown, starting graceful shutdown...");
			try {
				mainThread.join();
			} catch (InterruptedException e) {
				log.error("Error during shutdown", e);
			}
			log.info(" Application shut down gracefully");
		}));

		// Start the application
		SpringApplication.run(NotificationServiceApplication.class, args);

		log.info("""
     
                 Notification Service Started Successfully!            
                 Kafka Topics:                                            
                   • bookfair.reservation.created                           
                   • bookfair.user.registered                               
                                                                             
                 Services:                                                
                   • REST API: http://localhost:8082                        
                   • Kafka UI: http://localhost:8080                        
                   • MailHog UI: http://localhost:8025                      
                                                                             
                  Ready to process notifications!   
            """);
	}
}