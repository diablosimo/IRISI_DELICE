package sample.controller.admin;

import com.mysql.cj.core.util.StringUtils;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sample.MainAdmin;
import sample.bean.Categorie;
import sample.bean.Plat;
import sample.util.AlertUtil;
import sample.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class AddPlatController implements Initializable {
    @FXML
    private AnchorPane ap;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prixField;
    @FXML
    private ComboBox categorieCB = new ComboBox<Categorie>();
    @FXML
    private ImageView imageView;

    private FileChooser fileChooser = new FileChooser();
    private File file;
    private Image image;


    public void selectionnerImage(ActionEvent actionEvent) {
        file = fileChooser.showOpenDialog(ap.getScene().getWindow());
        if (file != null || !file.exists() || !file.isFile()) {
            image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

    public void ajouterPlat(ActionEvent actionEvent) {
        String nom = nomField.getText();
        String prix = prixField.getText();
        Categorie categorie = (Categorie) categorieCB.getValue();
        if (nom == null || nom.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout plât:", "Veuillez saisir un nom pour ce plât.");
        } else if (!Util.isNumeric(prix)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout plât:", "Veuillez saisir un chiffre acceptable.");
        } else if (categorie == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout plât:", "Veuillez selectionner une categorie pour ce plât.");
        }
        if (file == null) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout plât:", "Veuillez selectionner une image descriptive de ce plât.");
        } else {
            Plat plat = new Plat(nom, Double.parseDouble(prix), null, categorie.getId());
            try {
                int result = MainAdmin.od.ajouterPlat(plat, file);
                switch (result) {
                    case 1:
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout plât:", "Le plât a été ajouté avec succès.");
                        reset();
                        break;
                    case -1:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout plât:", "Image introuvable.");
                        break;
                    case -3:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout plât:", "Erreur lors de l'insertion.");
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void reset(){
        nomField.setText("");
        prixField.setText("");
        categorieCB.setValue(null);
        imageView.setImage(null);
        image=null;
        file=null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        try {
            categorieCB.setItems(FXCollections.observableArrayList(MainAdmin.od.findAllCategories()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
