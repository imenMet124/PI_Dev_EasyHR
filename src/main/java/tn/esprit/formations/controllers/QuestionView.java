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
import tn.esprit.formations.entities.Question;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.services.QuestionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuestionView {

    @FXML
    private TableView<Question> tableQuestions;

    @FXML
    private TableColumn<Question, String> textColumn;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnBack;

    private Quiz quiz;
    private final QuestionService questionService = new QuestionService();

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        if (this.quiz != null) {
            loadQuestions();
        } else {
            System.out.println("Quiz is null; cannot load questions.");
            tableQuestions.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing QuestionView...");
        textColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getText()));
        // Do not load questions here; let setQuiz handle it
    }

    private void loadQuestions() {
        if (quiz == null) {
            showErrorAlert("Error", "Quiz is not set; cannot load questions.");
            return;
        }

        try {
            List<Question> questions = questionService.getQuestionsForQuiz(quiz.getId());
            quiz.setQuestions(questions);
            System.out.println("Questions loaded: " + questions.size());
            if (questions.isEmpty()) {
                System.out.println("No questions found for this quiz.");
            }
            tableQuestions.setItems(FXCollections.observableArrayList(questions));
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error Loading Questions", "Could not load questions: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterQuestion(ActionEvent event) throws IOException {
        switchScene(event, "/fxml/admin/AjouterQuestion.fxml", null);
    }

    @FXML
    private void handleModifierQuestion(ActionEvent event) throws IOException {
        Question selected = tableQuestions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a question to modify.");
            return;
        }
        switchScene(event, "/fxml/admin/ModifierQuestion.fxml", selected);
    }

    @FXML
    private void handleSupprimerQuestion(ActionEvent event) {
        Question selected = tableQuestions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("No Selection", "Please select a question to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this question?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    questionService.supprimer(selected.getId());
                    tableQuestions.getItems().remove(selected);
                    quiz.getQuestions().remove(selected);
                    showInfoAlert("Success", "Question deleted successfully!");
                } catch (SQLException ex) {
                    showErrorAlert("Deletion Error", "Could not delete question: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        if (quiz == null) {
            showErrorAlert("Navigation Error", "Quiz information is missing.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/DetailsQuiz.fxml"));
        Parent root = loader.load();
        DetailsQuizController controller = loader.getController();
        controller.setQuiz(quiz);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void switchScene(ActionEvent event, String fxmlFile, Question selectedQuestion) throws IOException {
        if (quiz == null) {
            showErrorAlert("Navigation Error", "Quiz information is missing.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        if (selectedQuestion != null) {
            if (fxmlFile.contains("ModifierQuestion")) {
                ModifierQuestionController controller = loader.getController();
                controller.setQuestion(selectedQuestion);
                controller.setQuestionView(this);
            }
        } else if (fxmlFile.contains("AjouterQuestion")) {
            AjouterQuestionController controller = loader.getController();
            controller.setQuiz(quiz);
            controller.setQuestionView(this);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void refreshTable() {
        loadQuestions();
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