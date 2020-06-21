package sample.controller.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.MainClient;
import sample.bean.Commande;
import sample.helper.CommandeHelper;
import sample.helper.LigneCommandeHelper;
import sample.helper.PlatView;
import sample.util.Session;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MesCommandeController implements Initializable {
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



    List<CommandeHelper> commandes=new ArrayList<>();
    List<LigneCommandeHelper> ligneCommandes= new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        try {
            Long idClient= Session.getConClient().getId();
            commandes= MainClient.od.findCommandesOfConnectedClient(idClient);
            commandeTable.setItems(FXCollections.observableArrayList(commandes));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        commandeTable.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<CommandeHelper>() {
            @Override
            public void changed(ObservableValue<? extends CommandeHelper> observable, CommandeHelper oldValue, CommandeHelper newValue) {
                if (newValue!=null){
                    try {
                        initTable2();
                        ligneCommandes= MainClient.od.findLigneCommandesByCommandeID(newValue.getId());
                        ligneCommandeTable.setItems(FXCollections.observableArrayList(ligneCommandes));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }

    public void initTable() {
        dateCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        adresseLivraisonColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        montantTotalColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }
    public void initTable2() {
        platColumn.setCellValueFactory(new PropertyValueFactory<>("plat"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        prixUColumn.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        montantLigneColumn.setCellValueFactory(new PropertyValueFactory<>("montantLigne"));
    }
}
