package sample.controller.admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.MainAdmin;
import sample.bean.Categorie;
import sample.util.AlertUtil;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategorieController implements Initializable {

    @FXML
    private TextField newCategorieField;
    @FXML
    private TextField modifiedCategorieField;
    @FXML
    private ComboBox modifiedCategorieCB = new ComboBox<Categorie>();
    @FXML
    private ComboBox deletedCategorieCB = new ComboBox<Categorie>();

    List<Categorie> categories = new ArrayList<Categorie>();


    public void ajouterCategorie(ActionEvent actionEvent) {
        String libelle = newCategorieField.getText();
        if (libelle == null || libelle.isEmpty())
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie:", "Le champ de la nouvelle categorie est vide");
        try {
            int result = MainAdmin.od.ajouterCategorie(libelle);
            switch (result) {
                case -1:
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie:", "Le nom de la categorie n'est pas mentionné.");
                    break;
                case -2:
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie: ", "La categorie existe déjà.");
                    break;
                case -3:
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie: ", "Echec d'ajout.");
                    break;
                case 1:
                    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Succès", "Categorie: ", "Catégorie enregistrée avec succès.");
                    init();
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void modifierCategorie(ActionEvent actionEvent) {
        String libelle = modifiedCategorieField.getText();
        Categorie categorie = (Categorie) modifiedCategorieCB.getValue();
        if (libelle == null || libelle.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur0", "Categorie:", "Le nouveau nom de la categorie n'est pas mentionné.");

        } else if (categorie == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur.", "Categorie:", "La categorie à modifier n'est pas selectionnée.");
        } else {
            try {
                int result = MainAdmin.od.modifierCategorie(categorie, libelle);
                switch (result) {
                    case -2:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie:", "La categorie à modifier n'est pas selectionnée.");
                        break;
                    case -1:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie: ", "Le nouveau nom de la categorie n'est pas mentionné.");
                        break;
                    case -4:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie:", "La categorie a le même nom que celui proposé.");
                        break;
                    case -3:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie: ", "Echec de modification.");
                        break;
                    case 1:
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Succès", "Categorie: ", "Catégorie a été modifiée avec succès.");
                        init();
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void supprimerCategorie(ActionEvent actionEvent) {
        Categorie categorie = (Categorie) deletedCategorieCB.getValue();
        if (categorie == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie:", "La categorie à supprimer n'est pas selectionnée.");
        } else {
            try {
                int result = MainAdmin.od.supprimerCategorie(categorie);
                switch (result) {
                    case -1:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie:", "La categorie à supprimer n'est pas selectionnée.");
                        break;
                    case -2:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie: ", "La categorie selectionnée contient des plats.");
                        break;
                    case 1:
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Succès", "Categorie: ", "Catégorie a été supprimée .");
                        init();
                        break;
                    case -3:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Categorie: ", "Echec de  suppression .");
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(){
        try {
            categories = MainAdmin.od.findAllCategories();
            modifiedCategorieCB.setItems(FXCollections.observableArrayList(categories));
            deletedCategorieCB.setItems(FXCollections.observableArrayList(categories));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();

    }


}
