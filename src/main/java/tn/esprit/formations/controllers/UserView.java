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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserView {

    @FXML
    private TableView<Formation> tableFormations;

    @FXML
    private TableColumn<Formation, String> titreColumn;

    @FXML
    private TableColumn<Formation, String> descriptionColumn;

    @FXML
    private TableColumn<Formation, String> quizColumn;

    @FXML
    private Button btnDetails;

    @FXML
    private Button btnTakeQuiz;

    private final ServiceFormation serviceFormation = new ServiceFormation();

    @FXML
    public void initialize() {
        System.out.println("Initializing UserView...");
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
    private void handleDetails(ActionEvent event) throws IOException {
        Formation selected = tableFormations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a formation to view details.");
            return;
        }
        switchScene(event, "/fxml/user/UserDetailsFormation.fxml", selected);
    }

    @FXML
    private void handleTakeQuiz(ActionEvent event) throws IOException {
        Formation selected = tableFormations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a formation to take its quiz.");
            return;
        }
        if (selected.getQuiz() == null) {
            showInfoAlert("No Quiz", "This formation does not have an associated quiz.");
            return;
        }
        switchScene(event, "/fxml/user/TakeQuiz.fxml", selected);
    }

    private void switchScene(ActionEvent event, String fxmlFile, Formation selectedFormation) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        if (fxmlFile.contains("UserDetailsFormation")) {
            UserDetailsFormationController controller = loader.getController();
            controller.setFormation(selectedFormation);
        } else if (fxmlFile.contains("TakeQuiz")) {
            TakeQuizController controller = loader.getController();
            controller.setFormation(selectedFormation);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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