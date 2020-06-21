package sample.controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.MainClient;
import sample.bean.Client;
import sample.util.AlertUtil;
import sample.util.Session;
import sample.util.Util;

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
    NavbarController navbarController;

    public void seConnecter(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login.isEmpty() || password.isEmpty()|| !Util.validateEmail(login)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "Veuillez saisir un email valide.");
        } else {

            try {
                Object[] result = MainClient.od.seConnecter(login, password);
                navbarController = new NavbarController();
                switch ((int) result[0]) {
                    case 1:
                        Session.createAtrribute((Client) result[1], "connectedUser");
                        navbarController.gotoMenu(event);
                        break;
                    case -1:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "L'email n'est pas renseigné.");
                        break;
                    case -2:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "Le mot de passe n'est pas renseigné.");
                        break;
                    case -3:
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Connexion: ", "client introuvable.");
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

    public void openCreateAcount(ActionEvent actionEvent) throws IOException {
        MainClient.forward(actionEvent, "../../view/client/inscription.fxml", this.getClass());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void openResetPassword(ActionEvent actionEvent) throws IOException {
        MainClient.forward(actionEvent, "../../view/client/resetPassword.fxml", this.getClass());
    }
}
