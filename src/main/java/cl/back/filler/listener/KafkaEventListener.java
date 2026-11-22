package cl.back.filler.listener;

import cl.back.filler.config.KafkaTopicConfig;
import cl.back.filler.dto.AnaliticaMatchDTO;
import cl.back.filler.dto.ReaccionEventoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListener.class);

    // Consumidor 1: Procesa reacciones con control de reintentos (3 intentos)
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "true"
    )
    @KafkaListener(
            topics = KafkaTopicConfig.TOPIC_REACCIONES,
            groupId = "filler-reacciones-group"
    )
    public void consumirReaccion(ReaccionEventoDTO evento) {
        if (evento == null || evento.getPerfilId() == null || evento.getAccion() == null) {
            log.warn("[KAFKA CONSUMER - REACCIONES] Payload inválido o incompleto ignorado.");
            return;
        }

        log.info("[KAFKA CONSUMER - REACCIONES] Procesando {} hacia '{}' (ID: {}) | ¿Match?: {}",
                evento.getAccion(), evento.getNombrePerfil(), evento.getPerfilId(), evento.isEsMatch());
    }

    // Consumidor 2: Segundo Consumer Group leyendo el MISMO tópico para auditoría
    @KafkaListener(
            topics = KafkaTopicConfig.TOPIC_REACCIONES,
            groupId = "filler-auditoria-group"
    )
    public void auditarReaccion(ReaccionEventoDTO evento) {
        try {
            if (evento == null || evento.getPerfilId() == null) return;
            log.info("[KAFKA AUDITORÍA] Registro guardado -> Perfil: {} | Acción: {} | Fecha: {}",
                    evento.getNombrePerfil(), evento.getAccion(), evento.getFechaHora());
        } catch (Exception e) {
            log.error("[KAFKA AUDITORÍA ERROR] Error en auditoría: {}", e.getMessage());
        }
    }

    // Consumidor 3: Escucha el segundo tópico (Analítica) con reintentos
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "true"
    )
    @KafkaListener(
            topics = KafkaTopicConfig.TOPIC_ANALITICA,
            groupId = "filler-analitica-group"
    )
    public void consumirAnalitica(AnaliticaMatchDTO analitica) {
        if (analitica == null || analitica.getTipoEvento() == null) {
            log.warn("[KAFKA CONSUMER - ANALÍTICA] Evento de analítica vacío ignorado.");
            return;
        }

        log.info("[KAFKA CONSUMER - ANALÍTICA] Métrica recibida -> Tipo: {} | Perfil: {} | Detalle: {}",
                analitica.getTipoEvento(), analitica.getNombrePerfil(), analitica.getDetalle());
    }

    // Manejador de mensajes fallidos tras agotar los reintentos (Dead Letter Topic)
    @DltHandler
    public void manejarMensajeFallido(Object payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("[KAFKA DLT] Mensaje descartado tras agotar reintentos en tópico '{}': {}", topic, payload);
    }
}