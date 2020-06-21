package sample.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public  class AlertUtil {
    public static void loginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("connexion:");
        alert.setContentText("Veuillez s'authentifier");
        alert.showAndWait();
    }

    public static void nameNoSetAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Création:");
        alert.setContentText("Role non defini");
        alert.showAndWait();
    }

    public static void falseloginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Connexion:");
        alert.setContentText("Connexion echouée!");
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType alertType,String title,String header,String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Alert getCommandeConfirmationAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,content, ButtonType.YES,ButtonType.CANCEL);
        alert.setTitle("Confirmation de la Commande");
        alert.setContentText(content);
        return alert;
    }



    public static void passwordCreationNotSetAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText("Attention:");
        alert.setContentText("mot de passe non saisi.");
        alert.showAndWait();
    }

    public static void appCreationNotSetAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText("Attention:");
        alert.setContentText("schema ou package non saisi.");
        alert.showAndWait();
    }

    public static void noIdentificationOptionSetAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erruer");
        alert.setHeaderText("Erreur:");
        alert.setContentText("Aucun moyen d'identification n'est sélectionné.");
        alert.showAndWait();
    }
    public static void roleExistAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erruer");
        alert.setHeaderText("Erreur:");
        alert.setContentText("role existant dans la base de données");
        alert.showAndWait();
    }
    public static void noRoleSelectedForGrantAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erruer");
        alert.setHeaderText("Erreur:");
        alert.setContentText("Veuillez selectionner au moins un role.");
        alert.showAndWait();
    }
    public static void noGrantedSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erruer");
        alert.setHeaderText("Erreur:");
        alert.setContentText("Veuillez selectionner au moins un role ou un utilisateur.");
        alert.showAndWait();
    }

    public static void showCreateAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Création:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }
    public static void showAlterAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Modification:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }
    public static void showGrantAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Attribution:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }

    public static void showGrantError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur d'attribution:");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public static void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("modification:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }

    public static void showUpdateError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur de modification:");
        alert.setContentText(error);
        alert.showAndWait();
    }
    public static void showDelete() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Suppression:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }
    public static void showDeleteError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Suppression:");
        alert.setContentText(error);
        alert.showAndWait();
    }
}


