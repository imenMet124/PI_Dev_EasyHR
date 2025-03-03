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

public class ModifierQuizController {

    @FXML
    private TextField titleField;

    private Quiz quiz;
    private final QuizService quizService = new QuizService();
    private QuizView quizView;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        titleField.setText(quiz.getTitle());
    }

    public void setQuizView(QuizView quizView) {
        this.quizView = quizView;
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (quiz == null) return;

        quiz.setTitle(titleField.getText());

        try {
            quizService.modifier(quiz);
            showInfoAlert("Success", "Quiz modified successfully!");
            if (quizView != null) {
                quizView.refreshTable();
            }
            switchToQuizView(event);
        } catch (SQLException e) {
            showErrorAlert("Error Modifying Quiz", "Could not modify quiz: " + e.getMessage());
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