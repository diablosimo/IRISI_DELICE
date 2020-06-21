package sample.helper;

import java.io.Serializable;

public class LigneCommandeHelper implements Serializable {
    private long id;
    private String plat;
    private int quantite;
    private double prixUnitaire;
    private double montantLigne;

    public LigneCommandeHelper(long id, String plat, int quantite, double prixUnitaire, double montantLigne) {
        this.id = id;
        this.plat = plat;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = montantLigne;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getMontantLigne() {
        return montantLigne;
    }

    public void setMontantLigne(double montantLigne) {
        this.montantLigne = montantLigne;
    }
}
