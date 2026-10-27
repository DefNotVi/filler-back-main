package cl.back.filler.listener;

import cl.back.filler.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Component
@Slf4j
public class OrderEventListener {

    @RabbitListener(
        id = "ordersListener", 
        queues = RabbitMQConfig.QUEUE_NAME, 
        containerFactory = "orderListenerFactory"
    )
    public void processOrder(Object message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("Mensaje procesado exitosamente por OrderEventListener: {}", message);
            
            // Confirmación al broker (ACK)
            channel.basicAck(tag, false);
            
        } catch (Exception e) {
            log.error("Fallo al procesar el mensaje. Redirigiendo a DLQ: {}", e.getMessage());
            // Rejection/NACK sin requeue -> El broker envia el mensaje a la DLQ
            channel.basicNack(tag, false, false);
        }
    }
}