package sample.bean;

import java.io.File;
import java.io.Serializable;

public class Plat implements Serializable {
    private Long id;
    private String nom;
    private double prix;
    private File image;
    private Long categorieId;

    public Plat() {
    }

    public Plat(String nom, double prix, File image, Long categorieId) {
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.categorieId = categorieId;
    }

    public Plat(Long id, String nom, double prix, File image, Long categorieId) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.categorieId = categorieId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", image='" + image + '\'' +
                ", categorieId=" + categorieId +
                '}';
    }
}
