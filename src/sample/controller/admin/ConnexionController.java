package sample.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.MainAdmin;
import sample.MainClient;
import sample.bean.Admin;
import sample.util.AlertUtil;
import sample.util.Session;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ConnexionController implements Initializable {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    SideBarController sideBarController;

    public void seConnecter(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login.isEmpty() || password.isEmpty()) {
            AlertUtil.loginError();
        } else {

            try {
                Object[] result = MainAdmin.od.seConnecter(login, password);
                sideBarController = new SideBarController();
                switch ((int) result[0]) {
                    case 1:
                        Session.createAtrribute((Admin) result[1], "connectedAdmin");
                        sideBarController.gotoDashboard(event);
                        break;
                    case -1:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "Le nom d'utilisateur n'est pas renseigné.");
                        break;

                    case -2:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "Le mot de passe n'est pas renseigné.");
                        break;

                    case -3:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "Utilisateur introuvable.");
                        break;

                    case -4:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "Mot de passe incorrecte.");
                        break;

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void openCreateAcount(ActionEvent actionEvent) throws IOException {
        MainAdmin.forward(actionEvent, "../../view/admin/inscription.fxml", this.getClass());

    }


}
