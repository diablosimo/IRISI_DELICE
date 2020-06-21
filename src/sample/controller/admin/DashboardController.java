package sample.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sample.MainAdmin;

import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private PieChart catPieChart;
    @FXML
    private BarChart<?, ?> platBarChart;
    @FXML
    private CategoryAxis platAxis;
    @FXML
    private NumberAxis nbCmdAxis;

    @FXML
    private Label caption;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCategorieData();
        initPlatData();
    }

    private void initCategorieData() {
        ObservableList<PieChart.Data> catData = FXCollections.observableArrayList();

        try {
            HashMap<String, Integer> data = MainAdmin.od.findNbCommandeByCategorie();
            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    catData.add(new PieChart.Data(key, value));
                }
            }
            catPieChart.setData(catData);
            catPieChart.setTitle("Nombre des commandes par catégorie");
            catPieChart.setLegendSide(Side.BOTTOM);
            catPieChart.setLabelsVisible(true);

            for (final PieChart.Data data1 : catPieChart.getData()) {
                data1.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        caption.setLayoutX(e.getSceneX());
                        caption.setLayoutY(e.getSceneY());

                        caption.setText(String.valueOf(data1.getPieValue()));
                    }
                });

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initPlatData() {
        try {
            HashMap<String, Integer> data = MainAdmin.od.findNbCommandeByPlat();
            XYChart.Series series=new XYChart.Series<>();

            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    series.getData().add(new XYChart.Data(key, value));
                }
            }
            platBarChart.getData().addAll(series);
            platBarChart.setTitle("Nombre des commandes par plat");
            platBarChart.setAnimated(true);
            platBarChart.setLegendVisible(false);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
