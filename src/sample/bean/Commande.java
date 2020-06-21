package sample.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Commande implements Serializable {
    private Long id;
    private String statut;
    private LocalDateTime dateCommande;
    private LocalDateTime dateLivraison;
    private String adresse;
    private Long clientId;
    private Long adminId;
    private Long factureId;

    public Commande() {
    }

    public Commande(String adresse, Long clientId) {
        this.statut = "EN ATTENTE";
        this.adresse = adresse;
        this.clientId = clientId;
    }

    public Commande(String statut, LocalDateTime dateCommande, LocalDateTime dateLivraison, String adresse, Long clientId, Long adminId, Long factureId) {
        this.statut = statut;
        this.dateCommande = dateCommande;
        this.dateLivraison = dateLivraison;
        this.adresse = adresse;
        this.clientId = clientId;
        this.adminId = adminId;
        this.factureId = factureId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDateTime getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getFactureId() {
        return factureId;
    }

    public void setFactureId(Long factureId) {
        this.factureId = factureId;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", statut='" + statut + '\'' +
                ", dateCommande=" + dateCommande +
                ", dateLivraison=" + dateLivraison +
                ", adresse='" + adresse + '\'' +
                ", clientId=" + clientId +
                ", adminId=" + adminId +
                ", factureId=" + factureId +
                '}';
    }
}
