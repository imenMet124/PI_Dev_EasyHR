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
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.services.QuizService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuizView {

    @FXML
    private TableView<Quiz> tableQuizzes;

    @FXML
    private TableColumn<Quiz, String> titleColumn;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnDetails;

    @FXML
    private Button btnBack;

    private final QuizService quizService = new QuizService();

    @FXML
    public void initialize() {
        System.out.println("Initializing QuizView...");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        loadQuizzes();
    }

    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = quizService.afficher();
            System.out.println("Quizzes loaded: " + quizzes.size());
            if (quizzes.isEmpty()) {
                System.out.println("No quizzes found in the database.");
            }
            tableQuizzes.setItems(FXCollections.observableArrayList(quizzes));
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error Loading Quizzes", "Could not load quizzes: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterQuiz(ActionEvent event) throws IOException {
        switchScene(event, "/fxml/admin/AjouterQuiz.fxml", null);
    }

    @FXML
    private void handleModifierQuiz(ActionEvent event) throws IOException {
        Quiz selected = tableQuizzes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a quiz to modify.");
            return;
        }
        switchScene(event, "/fxml/admin/ModifierQuiz.fxml", selected);
    }

    @FXML
    private void handleSupprimerQuiz(ActionEvent event) {
        Quiz selected = tableQuizzes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a quiz to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this quiz?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    quizService.supprimer(selected.getId());
                    tableQuizzes.getItems().remove(selected);
                    showInfoAlert("Success", "Quiz deleted successfully!");
                } catch (SQLException ex) {
                    showErrorAlert("Deletion Error", "Could not delete quiz: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleDetailsQuiz(ActionEvent event) throws IOException {
        Quiz selected = tableQuizzes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a quiz to view details.");
            return;
        }
        switchScene(event, "/fxml/admin/DetailsQuiz.fxml", selected);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        // Navigate back to AdminView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void switchScene(ActionEvent event, String fxmlFile, Quiz selectedQuiz) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        if (selectedQuiz != null) {
            if (fxmlFile.contains("ModifierQuiz")) {
                ModifierQuizController controller = loader.getController();
                controller.setQuiz(selectedQuiz);
                controller.setQuizView(this);
            } else if (fxmlFile.contains("DetailsQuiz")) {
                DetailsQuizController controller = loader.getController();
                controller.setQuiz(selectedQuiz);
            }
        } else if (fxmlFile.contains("AjouterQuiz")) {
            AjouterQuizController controller = loader.getController();
            controller.setQuizView(this);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void refreshTable() {
        loadQuizzes();
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