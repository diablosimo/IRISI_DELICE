package sample.controller.admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.MainAdmin;
import sample.bean.Categorie;
import sample.bean.Plat;
import sample.helper.PlatHelper;
import sample.helper.PlatView;
import sample.util.AlertUtil;
import sample.util.Util;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchPlatController implements Initializable {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prixMinField;
    @FXML
    private TextField prixMaxField;
    @FXML
    private ComboBox categorieCB = new ComboBox<Categorie>();
    @FXML
    private TableView<PlatView> table = new TableView<>();
    @FXML
    private TableColumn<PlatView, String> nomCol = new TableColumn<>();
    @FXML
    private TableColumn<PlatView, String> prixCol = new TableColumn<>();
    @FXML
    private TableColumn<PlatView, String> categorieCol = new TableColumn<>();
    @FXML
    private TableColumn<PlatView, ImageView> imageCol = new TableColumn<>();

    List<PlatHelper> plats;
    List<PlatView> platViews;





    public void rechercherPlat(ActionEvent actionEvent) {
        plats = new ArrayList<>();
        platViews = new ArrayList<>();
        String nom = nomField.getText();
        Double prixMin = Double.valueOf(0);
        Double prixMax = Double.valueOf(0);
        if (Util.isNumeric(prixMinField.getText())) prixMin = Double.parseDouble(prixMinField.getText());
        if (Util.isNumeric(prixMaxField.getText())) prixMax = Double.parseDouble(prixMaxField.getText());
        Categorie categorie = (Categorie) categorieCB.getValue();

        try {
            plats = MainAdmin.od.findByCriteria(nom, prixMin, prixMax, categorie);
            if (plats != null && !plats.isEmpty()) {
                for (PlatHelper p : plats) {
                    platViews.add(new PlatView(p));
                }
            }
            table.setItems(FXCollections.observableArrayList(platViews));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        try {
            categorieCB.setItems(FXCollections.observableArrayList(MainAdmin.od.findAllCategories()));
            initTable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initTable() {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        categorieCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
    }

    public void modifierPlat(ActionEvent actionEvent) throws IOException {
        try {
            PlatView platView = table.getSelectionModel().getSelectedItem();
           // initUpdateForm(platView);
            open(actionEvent, "../../view/admin/modifierPlat.fxml", this.getClass(),platView);
        } catch (NullPointerException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Attention", "Modification", "Veuillez selectionner un plat");
        }
    }



    public static void open(ActionEvent actionEvent, String pageName, Class myClass,PlatView platView) throws IOException {
        FXMLLoader loader=new FXMLLoader(myClass.getResource(pageName));
        Parent parent = (Parent) loader.load();
        UpdatePlatController updatePlatController=loader.getController();
        updatePlatController.initUpdateForm(platView);
        Scene scene = new Scene(parent);
        Stage app_stage = new Stage ();
        app_stage.setScene(scene);
        app_stage.show();
    }

}
