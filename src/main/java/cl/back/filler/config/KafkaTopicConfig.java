package cl.back.filler.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String TOPIC_REACCIONES = "filler-reacciones-topic";
    public static final String TOPIC_ANALITICA = "filler-analitica-topic";

    // Tópico 1: Flujo de reacciones (Likes / Dislikes) en tiempo real
    @Bean
    public NewTopic reaccionesTopic() {
        return TopicBuilder.name(TOPIC_REACCIONES)
                .partitions(3) // 3 particiones para balanceo de carga con consumer groups
                .replicas(1)   // 1 réplica (porque tenemos 1 broker en docker-compose)
                .config(TopicConfig.RETENTION_MS_CONFIG, "604800000") // Retención de 7 días
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .build();
    }

    // Tópico 2: Flujo de analítica y auditoría de matches
    @Bean
    public NewTopic analiticaTopic() {
        return TopicBuilder.name(TOPIC_ANALITICA)
                .partitions(2) // 2 particiones
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "2592000000") // Retención de 30 días
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .build();
    }
}