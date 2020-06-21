package sample.controller.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.MainAdmin;
import sample.MainClient;
import sample.bean.Commande;
import sample.helper.CommandeHelper;
import sample.helper.LigneCommandeHelper;
import sample.util.AlertUtil;
import sample.util.Session;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TraitementCommandeController implements Initializable {
    @FXML
    private TableView<LigneCommandeHelper> ligneCommandeTable = new TableView<>();
    @FXML
    private TableColumn<LigneCommandeHelper, String> platColumn = new TableColumn<>();
    @FXML
    private TableColumn<LigneCommandeHelper, Integer> quantiteColumn = new TableColumn<>();
    @FXML
    private TableColumn<LigneCommandeHelper, Double> prixUColumn = new TableColumn<>();
    @FXML
    private TableColumn<LigneCommandeHelper, Double> montantLigneColumn = new TableColumn<>();
    @FXML
    private TableView<CommandeHelper> commandeTable = new TableView<>();
    @FXML
    private TableColumn<CommandeHelper, String> dateCommandeColumn = new TableColumn<>();
    @FXML
    private TableColumn<CommandeHelper, String> adresseLivraisonColumn = new TableColumn<>();
    @FXML
    private TableColumn<CommandeHelper, Double> montantTotalColumn = new TableColumn<>();
    @FXML
    private TableColumn<CommandeHelper, String> statutColumn = new TableColumn<>();
    @FXML
    private ComboBox statutCB=new ComboBox<String>();


    List<CommandeHelper> commandes=new ArrayList<>();
    List<LigneCommandeHelper> ligneCommandes= new ArrayList<>();

    CommandeHelper selectedCommand;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initStatutCB();
        initTable();
        try {
            commandes= MainAdmin.od.findCommandesNoneTraitedCommandes();
            commandeTable.setItems(FXCollections.observableArrayList(commandes));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        commandeTable.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<CommandeHelper>() {
            @Override
            public void changed(ObservableValue<? extends CommandeHelper> observable, CommandeHelper oldValue, CommandeHelper newValue) {
                if (newValue!=null){
                    statutCB.setValue(null);
                    statutCB.setDisable(false);
                    selectedCommand=newValue;
                    try {
                        initTable2();
                        System.out.println(newValue.getId());
                        ligneCommandes= MainAdmin.od.findLigneCommandesByCommandeID(newValue.getId());
                        ligneCommandeTable.setItems(FXCollections.observableArrayList(ligneCommandes));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else {
                    statutCB.setDisable(true);
                }
            }
        }));
    }

    private void initTable() {
        dateCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        adresseLivraisonColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        montantTotalColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }
    private void initTable2() {
        platColumn.setCellValueFactory(new PropertyValueFactory<>("plat"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        prixUColumn.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        montantLigneColumn.setCellValueFactory(new PropertyValueFactory<>("montantLigne"));
    }

    private void initStatutCB(){
        List<String> statut=new ArrayList<>();
        statut.add("EN PREPARATION");
        statut.add("EN LIVRAISON");
        statut.add("LIVREE");
        statutCB.setItems(FXCollections.observableArrayList(statut));;
    }

    public void modifierStatut(ActionEvent actionEvent) {
        String newStatut= (String) statutCB.getValue();
        if (newStatut==null || newStatut.equals(selectedCommand.getStatut())){
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Attention", "Traitement de la commande:", "Veuillez selectionner un nouveau statut.");
        }else{
            Commande commande=new Commande();
            commande.setId(selectedCommand.getId());
            commande.setStatut(selectedCommand.getStatut());
            DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            commande.setDateCommande(LocalDateTime.parse(selectedCommand.getDateCommande(),dtf));

            try {
                int result= MainAdmin.od.traiterCommande(commande,newStatut,Session.getConAdmin().getId());
                switch (result){
                    case 1:
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Succès", "Traitement de la commande:", "Modification de statut avec succes.");
                        MainAdmin.forward(actionEvent, "../../view/admin/traiterCommandes.fxml", this.getClass());
                        break;
                    case -1:
                        AlertUtil.showAlert(Alert.AlertType.WARNING, "Attention", "Traitement de la commande:", "Le statut choisi est inconvenable.");
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
