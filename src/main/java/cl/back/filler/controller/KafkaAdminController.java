package cl.back.filler.controller;

import cl.back.filler.service.KafkaAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
public class KafkaAdminController {

    private final KafkaAdminService kafkaAdminService;

    public KafkaAdminController(KafkaAdminService kafkaAdminService) {
        this.kafkaAdminService = kafkaAdminService;
    }

    // Listar todos los tópicos
    @GetMapping("/topics")
    public ResponseEntity<?> listarTopicos() {
        try {
            return ResponseEntity.ok(kafkaAdminService.listarTopicos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar tópicos: " + e.getMessage()));
        }
    }

    // Crear un tópico nuevo
    @PostMapping("/topics")
    public ResponseEntity<?> crearTopico(@RequestBody Map<String, Object> body) {
        try {
            String nombre = body.get("nombre").toString();
            int particiones = body.containsKey("particiones") ? Integer.parseInt(body.get("particiones").toString()) : 2;
            short replicas = body.containsKey("replicas") ? Short.parseShort(body.get("replicas").toString()) : 1;
            String retentionMs = body.containsKey("retentionMs") ? body.get("retentionMs").toString() : "604800000";

            kafkaAdminService.crearTopico(nombre, particiones, replicas, retentionMs);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "ok", "mensaje", "Tópico '" + nombre + "' creado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No se pudo crear el tópico: " + e.getMessage()));
        }
    }

    // Modificar particiones o retención de un tópico existente
    @PutMapping("/topics/{nombre}")
    public ResponseEntity<?> modificarTopico(@PathVariable String nombre, @RequestBody Map<String, Object> body) {
        try {
            Integer particiones = body.containsKey("particiones") ? Integer.parseInt(body.get("particiones").toString()) : null;
            String retentionMs = body.containsKey("retentionMs") ? body.get("retentionMs").toString() : null;

            kafkaAdminService.modificarTopico(nombre, particiones, retentionMs);
            return ResponseEntity.ok(Map.of("status", "ok", "mensaje", "Tópico '" + nombre + "' actualizado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al modificar tópico: " + e.getMessage()));
        }
    }

    // Eliminar un tópico
    @DeleteMapping("/topics/{nombre}")
    public ResponseEntity<?> eliminarTopico(@PathVariable String nombre) {
        try {
            kafkaAdminService.eliminarTopico(nombre);
            return ResponseEntity.ok(Map.of("status", "ok", "mensaje", "Tópico '" + nombre + "' eliminado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al eliminar tópico: " + e.getMessage()));
        }
    }

    // Listar e inspeccionar Consumer Groups
    @GetMapping("/consumer-groups")
    public ResponseEntity<?> listarConsumerGroups() {
        try {
            return ResponseEntity.ok(kafkaAdminService.listarConsumerGroups());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar consumer groups: " + e.getMessage()));
        }
    }
}