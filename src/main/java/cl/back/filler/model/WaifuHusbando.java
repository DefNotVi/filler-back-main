package cl.back.filler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity                  
@Table(name = "perfiles")
@Data                     
public class WaifuHusbando {

    @Id                                                 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    private String nombre;
    private String animeOOrigen;
    private String descripcion;
    private String fotoUrl;
    private int nivelDeOlor; 

    // Constructor vacío manual
    public WaifuHusbando() {
    }

    // Constructor completo manual (te amo tanto lombok pero te odio tanto lombok)
    public WaifuHusbando(Long id, String nombre, String animeOOrigen, String descripcion, String fotoUrl, int nivelDeOlor) {
        this.id = id;
        this.nombre = nombre;
        this.animeOOrigen = animeOOrigen;
        this.descripcion = descripcion;
        this.fotoUrl = fotoUrl;
        this.nivelDeOlor = nivelDeOlor;
    }
}