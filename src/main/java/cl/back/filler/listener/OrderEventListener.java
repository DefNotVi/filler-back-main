package cl.back.filler.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderEventListener {

    @RabbitListener(
        id = "ordersListener", 
        queues = "orders.queue", 
        containerFactory = "orderListenerFactory"
    )
    public void processOrder(String message) {
        log.info("Procesando mensaje en ordersListener: {}", message);
    }
}