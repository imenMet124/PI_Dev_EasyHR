package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Formation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserDetailsFormationController {

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

    @FXML
    private Button btnDownload;

    @FXML
    private Button btnBack;

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
    private void handleDownload(ActionEvent event) {
        try {
            String resourcePath = formation.getFilePath();
            URL resourceUrl = getClass().getResource(resourcePath);
            if (resourceUrl == null) {
                showErrorAlert("File Error", "PDF file not found: " + resourcePath);
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.setInitialFileName(new File(formation.getFilePath()).getName());
            FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(pdfFilter);

            Stage stage = (Stage) btnDownload.getScene().getWindow();
            File destinationFile = fileChooser.showSaveDialog(stage);

            if (destinationFile != null) {
                Files.copy(resourceUrl.openStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                showInfoAlert("Success", "PDF file saved to: " + destinationFile.getAbsolutePath());
            }
        } catch (IOException e) {
            showErrorAlert("Download Error", "Could not download PDF file: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/UserView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) titreLabel.getScene().getWindow();
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