package sample.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.MainAdmin;
import sample.util.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {
    @FXML
    private Label connectedAdmin;

    @FXML
    protected void gotoDashboard(ActionEvent event) throws IOException {
        MainAdmin.forward(event, "../../view/admin/dashboard.fxml", this.getClass());
    }
    @FXML
    protected void gotoAjouterPlat(ActionEvent event) throws IOException {
        MainAdmin.forward(event, "../../view/admin/ajouterPlat.fxml", this.getClass());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectedAdmin.setText(Session.getConnectedAdmin());
    }

    public void gotoCategories(ActionEvent actionEvent) throws IOException {
        MainAdmin.forward(actionEvent, "../../view/admin/categories.fxml", this.getClass());

    }
    public void gotoRechercherPlat(ActionEvent actionEvent) throws IOException {
        MainAdmin.forward(actionEvent, "../../view/admin/rechercherPlat.fxml", this.getClass());
    }
    public void gotoTraiterCommandes(ActionEvent actionEvent) throws IOException {
        MainAdmin.forward(actionEvent, "../../view/admin/traiterCommandes.fxml", this.getClass());
    }

    @FXML
    void disconnect(ActionEvent event) throws IOException {
        Session.delete("connectedAdmin");
        MainAdmin.forward(event, "../../view/admin/connexion.fxml", this.getClass());
    }

}
