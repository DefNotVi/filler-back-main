package cl.back.filler.service;

import cl.back.filler.config.KafkaTopicConfig;
import cl.back.filler.dto.AnaliticaMatchDTO;
import cl.back.filler.dto.ReaccionEventoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Publica en el tópico 1: Reacciones
    public void enviarEventoReaccion(ReaccionEventoDTO evento) {
        String key = String.valueOf(evento.getPerfilId()); // La key asegura que eventos del mismo perfil vayan a la misma partición
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_REACCIONES, key, evento)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("[KAFKA PRODUCER] Reacción enviada a '{}' | Partición: {} | Offset: {}",
                                KafkaTopicConfig.TOPIC_REACCIONES,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("[KAFKA PRODUCER ERROR] Falló envío de reacción: {}", ex.getMessage());
                    }
                });
    }

    // Publica en el tópico 2: Analítica
    public void enviarEventoAnalitica(AnaliticaMatchDTO analitica) {
        String key = String.valueOf(analitica.getPerfilId());
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_ANALITICA, key, analitica)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("[KAFKA PRODUCER] Analítica enviada a '{}' | Partición: {} | Offset: {}",
                                KafkaTopicConfig.TOPIC_ANALITICA,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("[KAFKA PRODUCER ERROR] Falló envío de analítica: {}", ex.getMessage());
                    }
                });
    }
}