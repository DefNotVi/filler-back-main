package cl.back.filler.controller;

import cl.back.filler.dto.QueueDTO;
import cl.back.filler.service.RabbitResourceManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rabbitmq")
public class RabbitMQController {

    private final RabbitResourceManager resourceManager;

    @Autowired
    public RabbitMQController(RabbitResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @PostMapping("/queues")
    public ResponseEntity<String> createQueue(@Valid @RequestBody QueueDTO dto) {
        resourceManager.createQueue(dto.getQueueName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cola '" + dto.getQueueName() + "' creada exitosamente");
    }

    @DeleteMapping("/queues/{queueName}")
    public ResponseEntity<String> deleteQueue(@PathVariable String queueName) {
        resourceManager.deleteQueue(queueName);
        return ResponseEntity.ok("Cola '" + queueName + "' eliminada");
    }

    @GetMapping("/queues/{queueName}")
    public ResponseEntity<?> getQueueInfo(@PathVariable String queueName) {
        var info = resourceManager.getQueueInfo(queueName);
        return info != null ? ResponseEntity.ok(info) : ResponseEntity.notFound().build();
    }
}