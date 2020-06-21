package sample.controller.admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.MainAdmin;
import sample.bean.Categorie;
import sample.bean.Plat;
import sample.helper.PlatView;
import sample.util.AlertUtil;
import sample.util.Util;

import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class UpdatePlatController implements Initializable {
    Plat plat;
    @FXML
    private TextField modifiedNomField;
    @FXML
    private TextField modifiedPrixField;
    @FXML
    private ComboBox modifiedCategorieCB = new ComboBox<Categorie>();
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane mp;
    private FileChooser fileChooser = new FileChooser();
    private File file;
    private Image image;


    public void initUpdateForm(PlatView platView) {
        modifiedNomField.setText(platView.getNom());
        modifiedPrixField.setText(Double.toString(platView.getPrix()));
        imageView.setImage(platView.getImage().getImage());
        modifiedCategorieCB.setValue(null);

        plat = new Plat();
        plat.setId(platView.getId());
    }

    public void selectionnerModifiedImage(ActionEvent actionEvent) {
        file = fileChooser.showOpenDialog(mp.getScene().getWindow());
        if (file != null || !file.exists() || !file.isFile()) {
            image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

    public void modifier(ActionEvent actionEvent) {
        String nom = modifiedNomField.getText();
        String prix = modifiedPrixField.getText();
        Categorie categorie = (Categorie) modifiedCategorieCB.getValue();
        if (nom == null || nom.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Modification plât:", "Veuillez saisir un nom pour ce plât.");
        } else if (!Util.isNumeric(prix)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Modification plât:", "Veuillez saisir un chiffre acceptable.");
        } else if (categorie == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Modification plât:", "Veuillez selectionner une categorie pour ce plât.");
        }
        else if (file == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Modification plât:", "Veuillez selectionner une image descriptive de ce plât.");
        } else {
            plat.setNom(nom);
            plat.setPrix(Double.parseDouble(prix));
            plat.setCategorieId(categorie.getId());
            plat.setImage(file);
            int result = 0;
            try {
                result = MainAdmin.od.modifierPlat(plat);
                switch (result) {
                    case 1:
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Succès", "Modification plât:", "Le plât a été modifié avec succès.");
                        reset();
                        ((Stage)mp.getScene().getWindow()).close();
                        break;

                    default:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Modification plât:", "Erreur lors de la modification.");
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();

    }

    private void reset() {
        plat = null;
        modifiedNomField.setText("");
        modifiedPrixField.setText("");
        imageView.setImage(null);
        modifiedCategorieCB.setValue(null);
    }

    private void init() {
        try {
            modifiedCategorieCB.setItems(FXCollections.observableArrayList(MainAdmin.od.findAllCategories()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

