package cl.back.filler.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessagePublisherService {

    private final RabbitTemplate rabbitTemplate;

    // Constructor...
    public MessagePublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String exchange, String routingKey, Object data) {
        rabbitTemplate.convertAndSend(exchange, routingKey, data);
        log.info("Mensaje enviado a exchange: '{}' con routingKey: '{}'", exchange, routingKey);
    }
}