package cl.back.filler.service;

import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListenerManagementService {

    private final RabbitListenerEndpointRegistry registry;

    public void pauseListener(String listenerId) {
        try {
            MessageListenerContainer container = registry.getListenerContainer(listenerId);
            if (container != null && container.isRunning()) {
                container.stop();
                log.info("Listener detenido/pausado: {}", listenerId);
            }
        } catch (Exception e) {
            log.error("Error al pausar listener {}: {}", listenerId, e.getMessage());
        }
    }

    public void resumeListener(String listenerId) {
        try {
            MessageListenerContainer container = registry.getListenerContainer(listenerId);
            if (container != null && !container.isRunning()) {
                container.start();
                log.info("Listener reanudado/iniciado: {}", listenerId);
            }
        } catch (Exception e) {
            log.error("Error al reanudar listener {}: {}", listenerId, e.getMessage());
        }
    }

    public List<String> getAllListenerIds() {
        return registry.getListenerContainerIds().stream().toList();
    }
}