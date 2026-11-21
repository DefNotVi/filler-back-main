package cl.back.filler.listener;

import cl.back.filler.config.KafkaTopicConfig;
import cl.back.filler.dto.AnaliticaMatchDTO;
import cl.back.filler.dto.ReaccionEventoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListener.class);

    // Consumidor 1: Procesa reacciones para el flujo principal (Consumer Group 1)
    @KafkaListener(
            topics = KafkaTopicConfig.TOPIC_REACCIONES,
            groupId = "filler-reacciones-group"
    )
    public void consumirReaccion(ReaccionEventoDTO evento) {
        try {
            // Validación de payload (requisito de manejo de errores)
            if (evento == null || evento.getPerfilId() == null || evento.getAccion() == null) {
                log.warn("[KAFKA CONSUMER - REACCIONES] Mensaje inválido o incompleto ignorado.");
                return;
            }

            log.info("[KAFKA CONSUMER - REACCIONES] Procesando {} hacia '{}' (ID: {}) | ¿Match?: {}",
                    evento.getAccion(), evento.getNombrePerfil(), evento.getPerfilId(), evento.isEsMatch());

        } catch (Exception e) {
            log.error("[KAFKA CONSUMER ERROR] Error procesando reacción sin interrumpir el servicio: {}", e.getMessage());
        }
    }

    // Consumidor 2: Segundo Consumer Group leyendo el MISMO tópico de reacciones para auditoría/logs
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

    // Consumidor 3: Escucha el segundo tópico (Analítica)
    @KafkaListener(
            topics = KafkaTopicConfig.TOPIC_ANALITICA,
            groupId = "filler-analitica-group"
    )
    public void consumirAnalitica(AnaliticaMatchDTO analitica) {
        try {
            if (analitica == null || analitica.getTipoEvento() == null) {
                log.warn("[KAFKA CONSUMER - ANALÍTICA] Evento de analítica vacío ignorado.");
                return;
            }

            log.info("[KAFKA CONSUMER - ANALÍTICA] Métrica recibida -> Tipo: {} | Perfil: {} | Detalle: {}",
                    analitica.getTipoEvento(), analitica.getNombrePerfil(), analitica.getDetalle());

        } catch (Exception e) {
            log.error("[KAFKA ANALÍTICA ERROR] Fallo al procesar métrica: {}", e.getMessage());
        }
    }
}
