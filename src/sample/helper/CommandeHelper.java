package sample.helper;

import java.io.Serializable;

public class CommandeHelper implements Serializable {
    private Long id;
    private String dateCommande;
    private String adresse;
    private double montantTotal;
    private String statut;


    public CommandeHelper(Long id, String dateCommande, String adresse, double montantTotal, String statut) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.adresse = adresse;
        this.montantTotal = montantTotal;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
