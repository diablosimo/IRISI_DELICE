package sample.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.MainAdmin;
import sample.MainClient;
import sample.bean.Admin;
import sample.bean.Client;
import sample.util.AlertUtil;
import sample.util.Util;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class InscriptionController implements Initializable {
    @FXML
    private TextField loginField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confPasswordField;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void inscrire(ActionEvent actionEvent) {
        String login = loginField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String pass = passwordField.getText();
        String confP = confPasswordField.getText();

        if(login==null||login.isEmpty()){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un login.");
        }else if(nom==null||nom.isEmpty()|| !Util.validateName(nom)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un nom valide.");
        }else if(prenom==null||prenom.isEmpty()|| !Util.validateName(prenom)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un prenom valide.");
        }else if(pass==null||pass.isEmpty()|| !Util.validatePassword(pass)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez saisir un mot de passe valide.");
        }else if(confP==null||confP.isEmpty()|| !pass.equals(confP)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez confirmer votre mot de passe.");
        }else{
            Admin admin=new Admin(login,pass,nom,prenom);
            try {
                int result= MainAdmin.od.inscrire(admin);
                if(result==-1){
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Veuillez remplir tous les champs.");
                }else if (result==-2){
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Inscription: ", "Admin existant.");
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
        MainClient.forward(actionEvent, "../../view/admin/connexion.fxml", this.getClass());
    }
}
