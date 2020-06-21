package sample.controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import sample.MainClient;
import sample.bean.Commande;
import sample.bean.LigneCommande;
import sample.bean.Plat;
import sample.helper.PlatHelper;
import sample.util.AlertUtil;
import sample.util.Session;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class CommandeController implements Initializable {
    @FXML
    private VBox vbox;
    public GridPane gridMenu;
    HashMap<String, List<Plat>> plats;
    List<LigneCommande> ligneCommandes;
    List<Spinner> spinners = new ArrayList<>();
    List<Plat> platList = new ArrayList<>();

    @FXML
    private TextArea adresseArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            adresseArea.setText(Objects.requireNonNull(Session.getConClient()).getAdresse());
            plats = MainClient.od.getMenuItems();
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }


        for (Map.Entry<String, List<Plat>> entry : plats.entrySet()) {
            vbox.getChildren().add(addGridMenuOfCategorie(entry.getValue(), entry.getKey()));
        }

    }

    private GridPane addGridMenuOfCategorie(List<Plat> plats, String categorie) {
        if (plats != null && plats.size() > 0) {
            Label catLabel = new Label(categorie);
            catLabel.setFont(new Font(30));
            vbox.getChildren().add(catLabel);
        }
        GridPane gridPane = new GridPane();
        int i = 0;
        int j = 0;
        for (int k = 0; k < plats.size(); k++) {
            if (k != 0 && k % 2 == 0) i++;
            if (k != 0 && (k + 1) % 2 == 0) {
                j = 1;
            } else {
                j = 0;
            }
            Label nomLabel = new Label(plats.get(k).getNom());
            nomLabel.setWrapText(true);
            Label prixLabel = new Label(plats.get(k).getPrix() + " DH");
            Spinner spinner = new Spinner(0, 100, 0, 1);
            spinners.add(spinner);
            platList.add(plats.get(k));
            vbox.getChildren().add(spinner);
            ImageView imageView = new ImageView(new Image(plats.get(k).getImage().toURI().toString(), 300, 150, false, false));
            HBox hBox = new HBox();
            VBox vBox = new VBox();


            vBox.getChildren().add(nomLabel);
            vBox.getChildren().add(prixLabel);
            vBox.getChildren().add(spinner);

            hBox.getChildren().add(vBox);
            hBox.getChildren().add(imageView);
            hBox.setMargin(vBox, new Insets(10, 10, 20, 10));
            hBox.setMargin(imageView, new Insets(10, 50, 20, 10));
            gridPane.add(hBox, j, i);
        }

        if (gridPane.getChildren().size() != 0) {
            if (gridPane.getChildren().size() % 2 == 0) {
                gridPane.setMargin(gridPane.getChildren().get(0), new Insets(50, 0, 0, 0));
                gridPane.setMargin(gridPane.getChildren().get(1), new Insets(50, 0, 0, 0));
            } else {
                gridPane.setMargin(gridPane.getChildren().get(0), new Insets(50, 0, 0, 0));
            }
        }

        return gridPane;
    }

    public void commander(ActionEvent actionEvent) {
        if (adresseArea == null || adresseArea.getText().isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Attention", "Commande: ", "Adresse de livraison non fournie.");
            return;
        }
        List<LigneCommande> ligneCommandes = new ArrayList<>();
        String msg = "Veuillez confirmer votre commande.\n";
        double montantTotal = 0;
        boolean unPlatChoisi = false;
        for (int i = 0; i < spinners.size(); i++) {
            if (unPlatChoisi == false && (Integer) spinners.get(i).getValue() > 0) unPlatChoisi = true;
            if ((Integer) spinners.get(i).getValue() > 0) {
                int qte = (Integer) spinners.get(i).getValue();
                ligneCommandes.add(new LigneCommande(qte, platList.get(i).getId()));
                double montantLigne = platList.get(i).getPrix() * qte;
                montantTotal += montantLigne;
                msg += platList.get(i).getNom() + " x " + qte + " (" + montantLigne + " DH) \n";
            }
        }
        msg += "total: " + montantTotal + " DH.";
        if (!unPlatChoisi) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Attention", "Commande: ", "Aucun plat n'a été choisi.");
        } else {
            Alert alert = AlertUtil.getCommandeConfirmationAlert(msg);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Commande commande = new Commande(adresseArea.getText(), Session.getConClient().getId());
                try {
                    int res = MainClient.od.commander(commande, ligneCommandes,Session.getConClient());
                    if (res == 1) {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Info", "Commande: ", "Vottre commande a bien été retenue.");
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Erreur", "Commande: ", "Une erreur est survenue lors de l'envoie de votre demande. Veuillez réessayer plus tard.");

                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
