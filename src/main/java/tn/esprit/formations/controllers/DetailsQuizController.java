package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Quiz;

import java.io.IOException;

public class DetailsQuizController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label questionsCountLabel;

    private Quiz quiz;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        titleLabel.setText(quiz.getTitle());
        questionsCountLabel.setText(String.valueOf(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/QuizView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleManageQuestions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/QuestionView.fxml"));
        Parent root = loader.load();
        QuestionView controller = loader.getController();
        controller.setQuiz(quiz);
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}