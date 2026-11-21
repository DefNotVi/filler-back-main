package cl.back.filler.dto;

import java.time.LocalDateTime;

public class AnaliticaMatchDTO {
    private String eventoId;
    private Long perfilId;
    private String nombrePerfil;
    private String tipoEvento; // Ej: "MATCH_LOGRADO" o "VOTO_REGISTRADO"
    private String detalle;
    private String timestamp;

    public AnaliticaMatchDTO() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public AnaliticaMatchDTO(String eventoId, Long perfilId, String nombrePerfil, String tipoEvento, String detalle) {
        this.eventoId = eventoId;
        this.perfilId = perfilId;
        this.nombrePerfil = nombrePerfil;
        this.tipoEvento = tipoEvento;
        this.detalle = detalle;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getEventoId() { return eventoId; }
    public void setEventoId(String eventoId) { this.eventoId = eventoId; }

    public Long getPerfilId() { return perfilId; }
    public void setPerfilId(Long perfilId) { this.perfilId = perfilId; }

    public String getNombrePerfil() { return nombrePerfil; }
    public void setNombrePerfil(String nombrePerfil) { this.nombrePerfil = nombrePerfil; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
