package sample.controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.MainClient;
import sample.bean.Client;
import sample.util.AlertUtil;
import sample.util.Util;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confPasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void inscrire(ActionEvent actionEvent) {
        String email = emailField.getText();
        String pass = passwordField.getText();
        String confP = confPasswordField.getText();

        if(email==null||email.isEmpty()|| !Util.validateEmail(email)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un email valide.");
        }else if(pass==null||pass.isEmpty()|| !Util.validatePassword(pass)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un mot de passe valide.");
        }else if(confP==null||confP.isEmpty()|| !pass.equals(confP)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez confirmer votre mot de passe.");
        }else{
            Client newClient=new Client(email,pass);
            try {
                int result= MainClient.od.resetPassword(newClient);
                if(result==-1){
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Client non trouvé");
                }else if (result==-2){
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Il s'agit de votre mot de passe courant.");
                }else if (result==1){
                    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Information", "Inscription: ", "Rèinitialisation avec succès.");
                    gotoConnexion(actionEvent);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void gotoConnexion(ActionEvent actionEvent) throws IOException {
        MainClient.forward(actionEvent, "../../view/client/connexion.fxml", this.getClass());
    }
}
