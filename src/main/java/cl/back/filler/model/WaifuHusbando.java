package cl.back.filler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                   // <- Esto le dice a Spring que es una tabla de BD
@Table(name = "perfiles") // <- Nombre de la tabla en H2
@Data                     // <- Lombok genera Getters, Setters y toString automáticamente
@NoArgsConstructor        // <- Lombok genera el constructor vacío que exige JPA
@AllArgsConstructor       // <- Lombok genera el constructor con todos los atributos
public class WaifuHusbando {

    @Id                                                 // <- Define la Clave Primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <- ID Autoincremental (1, 2, 3...)
    private Long id;
    
    private String nombre;
    private String animeOOrigen;
    private String descripcion;
    private String fotoUrl;
    private int nivelDeOlor; 
}