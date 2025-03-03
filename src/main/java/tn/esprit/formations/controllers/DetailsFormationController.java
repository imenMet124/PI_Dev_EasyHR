package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Formation;

import java.io.IOException;

public class DetailsFormationController {

    @FXML
    private Label titreLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label filePathLabel;

    @FXML
    private Label dateCreationLabel;

    @FXML
    private Label quizLabel;

    private Formation formation;

    public void setFormation(Formation formation) {
        this.formation = formation;
        titreLabel.setText(formation.getTitre());
        descriptionLabel.setText(formation.getDescription());
        filePathLabel.setText(formation.getFilePath());
        dateCreationLabel.setText(formation.getDateCreation().toString());
        quizLabel.setText(formation.getQuiz() != null ? formation.getQuiz().getTitle() : "No Quiz");
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) titreLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleManageQuiz(ActionEvent event) throws IOException {
        if (formation.getQuiz() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Quiz");
            alert.setContentText("This formation does not have an associated quiz.");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/QuestionView.fxml"));
        Parent root = loader.load();
        QuestionView controller = loader.getController();
        controller.setQuiz(formation.getQuiz());
        Stage stage = (Stage) titreLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}