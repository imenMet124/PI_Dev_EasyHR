package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Option;
import tn.esprit.formations.entities.Question;
import tn.esprit.formations.services.QuestionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModifierQuestionController {

    @FXML
    private TextArea textArea;

    @FXML
    private TextField optionsField;

    private Question question;
    private final QuestionService questionService = new QuestionService();
    private QuestionView questionView;

    public void setQuestion(Question question) {
        this.question = question;
        textArea.setText(question.getText());
        if (question.getOptions() != null) {
            String optionsText = question.getOptions().stream()
                    .map(Option::getText)
                    .collect(Collectors.joining(","));
            optionsField.setText(optionsText);
        }
    }

    public void setQuestionView(QuestionView questionView) {
        this.questionView = questionView;
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (question == null) {
            showErrorAlert("Error", "No question to modify.");
            return;
        }

        String text = textArea.getText();
        String optionsInput = optionsField.getText();

        if (text.isEmpty()) {
            showErrorAlert("Invalid Input", "Question text is required.");
            return;
        }

        if (optionsInput.isEmpty()) {
            showErrorAlert("Invalid Input", "At least one option is required.");
            return;
        }

        List<Option> options = new ArrayList<>();
        List<String> optionTexts = Arrays.asList(optionsInput.split(","));
        for (String optionText : optionTexts) {
            optionText = optionText.trim();
            if (!optionText.isEmpty()) {
                options.add(new Option(optionText, false));
            }
        }

        if (options.isEmpty()) {
            showErrorAlert("Invalid Input", "At least one valid option is required.");
            return;
        }

        question.setText(text);
        question.setOptions(options);

        try {
            questionService.modifier(question);
            showInfoAlert("Success", "Question modified successfully!");
            if (questionView != null) {
                questionView.refreshTable();
            }
            switchToQuestionView(event);
        } catch (SQLException e) {
            showErrorAlert("Error Modifying Question", "Could not modify question: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        switchToQuestionView(event);
    }

    private void switchToQuestionView(ActionEvent event) {
        try {
            if (question == null || question.getQuiz() == null) {
                showErrorAlert("Navigation Error", "Quiz information is missing.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/QuestionView.fxml"));
            Parent root = loader.load();
            QuestionView controller = loader.getController();
            controller.setQuiz(question.getQuiz()); // Ensure quiz is passed
            Stage stage = (Stage) textArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not return to Question View: " + e.getMessage());
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