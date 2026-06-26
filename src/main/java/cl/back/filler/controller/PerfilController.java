package cl.back.filler.controller;

import cl.back.filler.model.WaifuHusbando;
import cl.back.filler.repository.PerfilRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/perfiles")
//@CrossOrigin(origins = "*") // Permite que el frontend en Vite se conecte sin problemas de CORS
//comenté lo de arriba porque al contrario de lo que puse al inicio, ahora da error con el api gateway sdfjhljksfd
public class PerfilController {

    private final PerfilRepository perfilRepository;

    // Inyecta el repositorio para interactuar con la Base de Datos H2
    public PerfilController(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
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
                    .contentType(MediaType.IMAGE_JPEG) // JPEG cubre la gran mayoría de formatos estables de internet
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
        String accion = payload.get("accion").toString(); // "LIKE" o "DISLIKE"

        // Lógica satírica de respuesta rápida
        if ("LIKE".equalsIgnoreCase(accion)) {
            System.out.println("Le diste corazón al ID: " + perfilId);
            
            // Simula un Match aleatorio del 50% para hacerlo divertido
            boolean esMatch = Math.random() > 0.5; 
            return Map.of(
                "status", "ok",
                "mensaje", esMatch ? "¡Felicidades! ¡Es un Match otaku compulsivo!" : "Le diste like, pero te dejó en visto."
            );
        } else {
            System.out.println("Rechazaste al ID: " + perfilId);
            return Map.of("status", "ok", "mensaje", "shiiiiiiiizaaaaaa");
        }
    }
}