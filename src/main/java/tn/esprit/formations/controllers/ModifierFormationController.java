package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Formation;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.services.QuizService;
import tn.esprit.formations.services.ServiceFormation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModifierFormationController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField filePathField;

    @FXML
    private ComboBox<Quiz> quizComboBox;

    private Formation formation;
    private final ServiceFormation serviceFormation = new ServiceFormation();
    private final QuizService quizService = new QuizService();
    private AdminView adminView;

    public void setFormation(Formation formation) {
        this.formation = formation;
        titreField.setText(formation.getTitre());
        descriptionArea.setText(formation.getDescription());
        filePathField.setText(formation.getFilePath());
        if (formation.getQuiz() != null) {
            quizComboBox.setValue(formation.getQuiz());
        }
    }

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    @FXML
    public void initialize() {
        try {
            List<Quiz> quizzes = quizService.afficher();
            quizComboBox.getItems().addAll(quizzes);
            quizComboBox.setCellFactory(param -> new ListCell<Quiz>() {
                @Override
                protected void updateItem(Quiz item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTitle());
                    }
                }
            });
            quizComboBox.setButtonCell(new ListCell<Quiz>() {
                @Override
                protected void updateItem(Quiz item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Select a Quiz");
                    } else {
                        setText(item.getTitle());
                    }
                }
            });
            quizComboBox.setPromptText("Select a Quiz");
        } catch (SQLException e) {
            showErrorAlert("Error Loading Quizzes", "Could not load quizzes: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (formation == null) {
            showErrorAlert("Error", "No formation to modify.");
            return;
        }

        formation.setTitre(titreField.getText());
        formation.setDescription(descriptionArea.getText());
        formation.setFilePath(filePathField.getText());
        formation.setQuiz(quizComboBox.getValue());

        try {
            serviceFormation.modifier(formation);
            showInfoAlert("Success", "Formation modified successfully!");
            if (adminView != null) {
                adminView.refreshTable();
            }
            switchToAdminView(event);
        } catch (SQLException e) {
            showErrorAlert("Error Modifying Formation", "Could not modify formation: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        switchToAdminView(event);
    }

    private void switchToAdminView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not return to Admin View: " + e.getMessage());
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