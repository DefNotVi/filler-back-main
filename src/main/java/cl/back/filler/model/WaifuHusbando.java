package cl.back.filler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                  
@Table(name = "perfiles") // <- Nombre de la tabla en H2
@Data                     
@NoArgsConstructor       
@AllArgsConstructor       
public class WaifuHusbando {

    @Id                                                 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    private String nombre;
    private String animeOOrigen;
    private String descripcion;
    private String fotoUrl;
    private int nivelDeOlor; 
}