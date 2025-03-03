package tn.esprit.formations.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Formation;
import tn.esprit.formations.services.ServiceFormation;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class AdminView {

    @FXML
    private TableView<Formation> tableFormations;

    @FXML
    private TableColumn<Formation, String> titreColumn;

    @FXML
    private TableColumn<Formation, String> descriptionColumn;

    @FXML
    private TableColumn<Formation, String> quizColumn; // New column for quiz title

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnDetails;

    @FXML
    private Button btnOuvrir;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnQuizzes;

    private final ServiceFormation serviceFormation = new ServiceFormation();

    @FXML
    public void initialize() {
        System.out.println("Initializing AdminView...");
        titreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        quizColumn.setCellValueFactory(cellData -> {
            Formation formation = cellData.getValue();
            return new SimpleStringProperty(formation.getQuiz() != null ? formation.getQuiz().getTitle() : "No Quiz");
        });

        loadFormations();
    }

    private void loadFormations() {
        try {
            List<Formation> formations = serviceFormation.afficher();
            System.out.println("Formations loaded: " + formations.size());
            if (formations.isEmpty()) {
                System.out.println("No formations found in the database.");
            }
            tableFormations.setItems(FXCollections.observableArrayList(formations));
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error Loading Formations", "Could not load formations: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterFormation(ActionEvent event) throws IOException {
        switchScene(event, "/fxml/admin/AjouterFormation.fxml", null);
    }

    @FXML
    private void handleModifierFormation(ActionEvent event) throws IOException {
        Formation selected = tableFormations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a formation to modify.");
            return;
        }
        switchScene(event, "/fxml/admin/ModifierFormation.fxml", selected);
    }

    @FXML
    private void handleSupprimerFormation(ActionEvent event) {
        Formation selected = tableFormations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a formation to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this formation?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    serviceFormation.supprimer(selected.getId());
                    tableFormations.getItems().remove(selected);
                    showInfoAlert("Success", "Formation deleted successfully!");
                } catch (SQLException ex) {
                    showErrorAlert("Deletion Error", "Could not delete formation: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleDetailsFormation(ActionEvent event) throws IOException {
        Formation selected = tableFormations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a formation to view details.");
            return;
        }
        switchScene(event, "/fxml/admin/DetailsFormation.fxml", selected);
    }

    @FXML
    private void handleOuvrirFormation(ActionEvent event) {
        Formation selected = tableFormations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a formation to open.");
            return;
        }

        try {
            String resourcePath = selected.getFilePath();
            URL resourceUrl = getClass().getResource(resourcePath);
            if (resourceUrl == null) {
                showErrorAlert("File Error", "PDF file not found: " + resourcePath);
                return;
            }
            Path tempFile = Files.createTempFile("formation_", ".pdf");
            Files.copy(resourceUrl.openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(tempFile.toFile());
            } else {
                showErrorAlert("Desktop Error", "Opening files is not supported on this system.");
            }
            tempFile.toFile().deleteOnExit();
        } catch (IOException e) {
            showErrorAlert("File Error", "Could not open PDF file: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadFormations();
    }

    @FXML
    private void handleQuizzes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/QuizView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void switchScene(ActionEvent event, String fxmlFile, Formation selectedFormation) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        if (fxmlFile.contains("AjouterFormation")) {
            AjouterFormationController controller = loader.getController();
            controller.setAdminView(this);
        } else if (fxmlFile.contains("ModifierFormation")) {
            ModifierFormationController controller = loader.getController();
            controller.setFormation(selectedFormation);
            controller.setAdminView(this);
        } else if (fxmlFile.contains("DetailsFormation")) {
            DetailsFormationController controller = loader.getController();
            controller.setFormation(selectedFormation);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void refreshTable() {
        loadFormations();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}