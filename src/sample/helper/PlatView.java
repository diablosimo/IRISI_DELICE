package sample.helper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class PlatView {
    private Long id;
    private String nom;
    private double prix;
    private ImageView image;
    private String categorie;

    public PlatView(PlatHelper platHelper) {
        this.id = platHelper.getId();
        this.nom = platHelper.getNom();
        this.prix = platHelper.getPrix();
        ImageView imageView=
        this.image = new ImageView(new Image(platHelper.getImage().toURI().toString(),300,150,false,false));
        this.categorie = platHelper.getCategorie();
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

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
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
        return "PlatView{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", image=" + image +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
