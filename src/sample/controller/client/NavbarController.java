package sample.controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sample.MainAdmin;
import sample.MainClient;
import sample.util.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {

    @FXML
    private Label usernameLabel;

    @FXML
    protected void gotoDashboard(ActionEvent event) throws IOException {
        MainClient.forward(event, "../../view/client/dashboard.fxml", this.getClass());
    }

    @FXML
    protected void gotoMenu(ActionEvent event) throws IOException {
        MainClient.forward(event, "../../view/client/menu.fxml", this.getClass());
    }

    @FXML
    protected void gotoCommandes(ActionEvent event) throws IOException {
        MainClient.forward(event, "../../view/client/commandes.fxml", this.getClass());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameLabel.setText(Session.getConnectedClient());
    }

    @FXML
    void disconnect(ActionEvent event) throws IOException {
        Session.delete("connectedUser");
        MainClient.forward(event, "../../view/client/connexion.fxml", this.getClass());
    }



}
