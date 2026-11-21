package cl.back.filler.dto;

public class ConsumerGroupDTO {
    private String groupId;
    private String estado;
    private int miembrosActivos;

    public ConsumerGroupDTO(String groupId, String estado, int miembrosActivos) {
        this.groupId = groupId;
        this.estado = estado;
        this.miembrosActivos = miembrosActivos;
    }

    public String getGroupId() { return groupId; }
    public String getEstado() { return estado; }
    public int getMiembrosActivos() { return miembrosActivos; }
}