package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.services.QuizService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AjouterQuizController {

    @FXML
    private TextField titleField;

    private final QuizService quizService = new QuizService();
    private QuizView quizView;

    public void setQuizView(QuizView quizView) {
        this.quizView = quizView;
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        String title = titleField.getText();

        if (title.isEmpty()) {
            showErrorAlert("Invalid Input", "Title is required.");
            return;
        }

        Quiz quiz = new Quiz(title, new ArrayList<>());
        try {
            quizService.ajouter(quiz);
            showInfoAlert("Success", "Quiz added successfully!");
            if (quizView != null) {
                quizView.refreshTable();
            }
            switchToQuizView(event);
        } catch (SQLException e) {
            showErrorAlert("Error Adding Quiz", "Could not add quiz: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        switchToQuizView(event);
    }

    private void switchToQuizView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/QuizView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not return to Quiz View: " + e.getMessage());
        }
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