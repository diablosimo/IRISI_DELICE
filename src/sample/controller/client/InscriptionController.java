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

public class InscriptionController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confPasswordField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea adresseField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void inscrire(ActionEvent actionEvent) {
        String email = emailField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String pass = passwordField.getText();
        String confP = confPasswordField.getText();
        String phone = phoneField.getText();
        String adresse = adresseField.getText();
        if(email==null||email.isEmpty()|| !Util.validateEmail(email)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un email valide.");
        }else if(nom==null||nom.isEmpty()|| !Util.validateName(nom)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un nom valide.");
        }else if(prenom==null||prenom.isEmpty()|| !Util.validateName(prenom)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un prenom valide.");
        }else if(pass==null||pass.isEmpty()|| !Util.validatePassword(pass)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un mot de passe valide.");
        }else if(confP==null||confP.isEmpty()|| !pass.equals(confP)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez confirmer votre mot de passe.");
        }else if(phone==null||phone.isEmpty()|| !Util.validatePhoneNumber(phone)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un numéro de tééphone valide.");
        }else if(adresse==null||adresse.isEmpty()){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir une adresse valide.");
        }else{
            Client newClient=new Client(nom,prenom,email,pass,adresse,phone);
            try {
                int result= MainClient.od.inscrire(newClient);
                if(result==-1){
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez sremplir tous les champs.");
                }else if (result==-2){
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "client existant.");
                }else if (result==1){
                    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Information", "Inscription: ", "Inscription avec succès.");
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
