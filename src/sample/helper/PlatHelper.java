package sample.helper;

import java.io.File;
import java.io.Serializable;

public class PlatHelper implements Serializable {
    private Long id;
    private String nom;
    private double prix;
    private File image;
    private String categorie;

    public PlatHelper(Long id, String nom, double prix, File image, String categorie) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.categorie = categorie;
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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "PlatHelper{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", image=" + image +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
