package cl.back.filler.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessagePublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String routingKey, Object data) {
        rabbitTemplate.convertAndSend(exchange, routingKey, data);
        log.info("Mensaje enviado a exchange: '{}' con routingKey: '{}'", exchange, routingKey);
    }
}