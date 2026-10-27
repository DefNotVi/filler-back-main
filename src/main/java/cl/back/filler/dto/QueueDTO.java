package cl.back.filler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueueDTO {
    @NotBlank(message = "El nombre de la cola no puede estar vacío")
    private String queueName;
}
