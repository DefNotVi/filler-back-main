package cl.back.filler.dto;

import java.time.LocalDateTime;

public class ReaccionEventoDTO {
    private Long perfilId;
    private String nombrePerfil;
    private String animeOOrigen;
    private String accion; // "LIKE" o "DISLIKE"
    private boolean esMatch;
    private String fechaHora;

    public ReaccionEventoDTO() {
        this.fechaHora = LocalDateTime.now().toString();
    }

    public ReaccionEventoDTO(Long perfilId, String nombrePerfil, String animeOOrigen, String accion, boolean esMatch) {
        this.perfilId = perfilId;
        this.nombrePerfil = nombrePerfil;
        this.animeOOrigen = animeOOrigen;
        this.accion = accion;
        this.esMatch = esMatch;
        this.fechaHora = LocalDateTime.now().toString();
    }

    public Long getPerfilId() { return perfilId; }
    public void setPerfilId(Long perfilId) { this.perfilId = perfilId; }

    public String getNombrePerfil() { return nombrePerfil; }
    public void setNombrePerfil(String nombrePerfil) { this.nombrePerfil = nombrePerfil; }

    public String getAnimeOOrigen() { return animeOOrigen; }
    public void setAnimeOOrigen(String animeOOrigen) { this.animeOOrigen = animeOOrigen; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public boolean isEsMatch() { return esMatch; }
    public void setEsMatch(boolean esMatch) { this.esMatch = esMatch; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}