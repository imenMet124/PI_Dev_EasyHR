package tn.esprit.tache.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainController {

    @FXML private BorderPane mainPane;
    @FXML private ImageView logo; // ✅ Logo ImageView

    @FXML
    public void initialize() {
        // ✅ Load logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/images/EasyHR-Logo.png"));
        logo.setImage(logoImage);
        openEmployeView();

    }

    @FXML
    private void openEmployeView() {
        loadView("/views/employe.fxml");
    }

    @FXML
    private void openProjetView() {
        loadView("/views/projet.fxml");
    }

    @FXML
    private void openTacheView() {
        loadView("/views/tache.fxml");
    }

    @FXML
    private void openAffectationView() {
        loadView("/views/affectation.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Handle Logout Action
    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to confirm logout.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ✅ Close Application Window
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.close();
        }
    }
}
