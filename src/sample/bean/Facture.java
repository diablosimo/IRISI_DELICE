package sample.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Facture implements Serializable {
    private Long id;
    private LocalDateTime dateFacturation;
    private Long commandeId;

    public Facture() {
    }

    public Facture(LocalDateTime dateFacturation, Long commandeId) {
        this.dateFacturation = dateFacturation;
        this.commandeId = commandeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(LocalDateTime dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "id=" + id +
                ", dateFacturation=" + dateFacturation +
                ", commandeId=" + commandeId +
                '}';
    }
}
