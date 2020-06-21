package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.bean.Client;
import sample.service.AdminService;
import sample.service.ClientService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MainClient extends Application {
    public static String url = "rmi://localhost/client_delice";
    public static ClientService od;

    static {
        try {
            od = (ClientService) Naming.lookup(url);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/client/connexion.fxml"));
        primaryStage.setTitle("IRISI Declice");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.getIcons().add(new Image("/sample/view/client/resources/logo.png"));
        primaryStage.show();
    }

    public static void forward(ActionEvent actionEvent, String pageName, Class myClass) throws IOException {
        Parent parent = FXMLLoader.load(myClass.getResource(pageName));
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(scene);

        app_stage.show();
    }

    public static void open(ActionEvent actionEvent, String pageName, Class myClass) throws IOException {
        Parent parent = FXMLLoader.load(myClass.getResource(pageName));
        Scene scene = new Scene(parent);
        Stage app_stage = new Stage ();
        app_stage.setScene(scene);

        app_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
