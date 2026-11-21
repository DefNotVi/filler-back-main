package cl.back.filler.controller;

import cl.back.filler.dto.AnaliticaMatchDTO;
import cl.back.filler.dto.ReaccionEventoDTO;
import cl.back.filler.model.WaifuHusbando;
import cl.back.filler.repository.PerfilRepository;
import cl.back.filler.service.KafkaProducerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/perfiles")
//@CrossOrigin(origins = "*") // Permite que el frontend en Vite se conecte sin problemas de CORS
//comenté lo de arriba porque al contrario de lo que puse al inicio, ahora da error con el api gateway sdfjhljksfd
public class PerfilController {

    private final PerfilRepository perfilRepository;
    private final KafkaProducerService kafkaProducerService;

    // Inyecta el repositorio para interactuar con la Base de Datos H2
    public PerfilController(PerfilRepository perfilRepository, KafkaProducerService kafkaProducerService) {
        this.perfilRepository = perfilRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    // GET: http://localhost:8080/api/perfiles
    // El frontend llama aquí para cargar la lista de cartas con las URLs de verdad
    @GetMapping
    public List<WaifuHusbando> listarPerfiles() {
        return perfilRepository.findAll();
    }

    // GET: http://localhost:8080/api/perfiles/proxy-image?url=...
    // Descarga la imagen desde internet por detrás para evadir bloqueos de CORS u otra cosa
    @GetMapping("/proxy-image")
    public ResponseEntity<byte[]> proxyImagen(@RequestParam String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (Exception e) {
            System.out.println("Error en el proxy de imagen: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // POST: http://localhost:8080/api/perfiles/reaccion
    // El frontend envia aquí la decisión (LIKE o DISLIKE)
    @PostMapping("/reaccion")
    public Map<String, String> reaccionarAPerfil(@RequestBody Map<String, Object> payload) {
        Long perfilId = Long.valueOf(payload.get("perfilId").toString());
        String accion = payload.get("accion").toString();

        // Busca el perfil en DB para dejar mejor el evento con nombre y anime
        WaifuHusbando perfil = perfilRepository.findById(perfilId).orElse(null);
        String nombrePerfil = perfil != null ? perfil.getNombre() : "Desconocido";
        String anime = perfil != null ? perfil.getAnimeOOrigen() : "Desconocido";

        boolean esMatch = "LIKE".equalsIgnoreCase(accion) && (Math.random() > 0.5);

        // 1. Publicar evento en Topic de Reacciones
        ReaccionEventoDTO eventoReaccion = new ReaccionEventoDTO(
                perfilId, nombrePerfil, anime, accion.toUpperCase(), esMatch
        );
        kafkaProducerService.enviarEventoReaccion(eventoReaccion);

        // 2. Publicar evento en Topic de Analítica
        String tipoEvento = esMatch ? "MATCH_LOGRADO" : "VOTO_" + accion.toUpperCase();
        String detalle = esMatch
                ? "¡Match conseguido con " + nombrePerfil + "!"
                : "Usuario reaccionó con " + accion + " a " + nombrePerfil;

        AnaliticaMatchDTO eventoAnalitica = new AnaliticaMatchDTO(
                UUID.randomUUID().toString(), perfilId, nombrePerfil, tipoEvento, detalle
        );
        kafkaProducerService.enviarEventoAnalitica(eventoAnalitica);

        if ("LIKE".equalsIgnoreCase(accion)) {
            return Map.of(
                "status", "ok",
                "mensaje", esMatch ? "¡Felicidades! ¡Es un Match otaku compulsivo!" : "Le diste like, pero te dejó en visto."
            );
        } else {
            return Map.of("status", "ok", "mensaje", "shiiiiiiiizaaaaaa");
        }
    }
}