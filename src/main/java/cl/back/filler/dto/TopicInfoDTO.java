package cl.back.filler.dto;

public class TopicInfoDTO {
    private String nombre;
    private int particiones;
    private int replicacion;

    public TopicInfoDTO(String nombre, int particiones, int replicacion) {
        this.nombre = nombre;
        this.particiones = particiones;
        this.replicacion = replicacion;
    }

    public String getNombre() { return nombre; }
    public int getParticiones() { return particiones; }
    public int getReplicacion() { return replicacion; }
}
