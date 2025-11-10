package lk.bookfair.notification.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.topics.reservation}")
    private String reservationTopic;

    @Value("${app.kafka.topics.registration}")
    private String registrationTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic reservationTopic() {
        return TopicBuilder
                .name(reservationTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder
                .name(registrationTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}