package cl.back.filler.controller;

import cl.back.filler.config.RabbitMQConfig;
import cl.back.filler.service.MessagePublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final MessagePublisherService publisherService;

    public OrderController(MessagePublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendOrder(@RequestBody OrderRequest request) {
        publisherService.sendMessage(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                request
        );
        return ResponseEntity.ok(Map.of("status", "ok", "message", "Evento recibido y publicado en RabbitMQ"));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Map.of("status", "ONLINE", "message", "Servicio de órdenes funcionando correctamente"));
    }

    // DTO interno con Getters/Setters (Gracias Lombok por tan poco...)
    public static class OrderRequest {
        private String orderId;
        private String customerName;
        private String detalles;

        public OrderRequest() {
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getDetalles() {
            return detalles;
        }

        public void setDetalles(String detalles) {
            this.detalles = detalles;
        }
    }
}