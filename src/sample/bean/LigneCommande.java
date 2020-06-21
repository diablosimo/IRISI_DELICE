package sample.bean;

import java.io.Serializable;

public class LigneCommande implements Serializable {
    private Long id;
    private int quantite;
    private Long platId;
    private Long commandeId;

    public LigneCommande() {
    }

    public LigneCommande(int quantite, Long platId, Long commandeId) {
        this.quantite = quantite;
        this.platId = platId;
        this.commandeId = commandeId;
    }

    public LigneCommande(int quantite, Long platId) {
        this.quantite = quantite;
        this.platId = platId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Long getPlatId() {
        return platId;
    }

    public void setPlatId(Long platId) {
        this.platId = platId;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", platId=" + platId +
                ", commandeId=" + commandeId +
                '}';
    }
}
