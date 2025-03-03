package tn.esprit.formations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Formation;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.services.QuestionService;
import tn.esprit.formations.services.QuizService;
import tn.esprit.formations.services.ServiceFormation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AjouterFormationController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField filePathField;

    @FXML
    private Button browseButton;

    @FXML
    private ComboBox<Quiz> quizComboBox; // New ComboBox for selecting a quiz

    private final ServiceFormation serviceFormation = new ServiceFormation();
    private final QuizService quizService = new QuizService();
    private AdminView adminView;

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    @FXML
    public void initialize() {
        // Load quizzes into the ComboBox
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
    private void handleAjouter(ActionEvent event) {
        String titre = titreField.getText();
        String description = descriptionArea.getText();
        String filePath = filePathField.getText();
        Quiz selectedQuiz = quizComboBox.getValue();

        if (titre.isEmpty() || description.isEmpty()) {
            showErrorAlert("Invalid Input", "Title and description are required.");
            return;
        }

        if (filePath.isEmpty() || !filePath.toLowerCase().endsWith(".pdf")) {
            showErrorAlert("Invalid File", "Please select a valid PDF file.");
            return;
        }

        String newFilePath = copyPdfToResources(filePath);
        if (newFilePath == null) {
            showErrorAlert("File Copy Error", "Could not copy the PDF file to resources.");
            return;
        }

        Formation formation = new Formation(titre, description, newFilePath, LocalDateTime.now());
        formation.setQuiz(selectedQuiz); // Set the selected quiz
        try {
            serviceFormation.ajouter(formation);
            showInfoAlert("Success", "Formation added successfully!");
            if (adminView != null) {
                adminView.refreshTable();
            }
            switchToAdminView(event);
        } catch (SQLException e) {
            showErrorAlert("Error Adding Formation", "Could not add formation: " + e.getMessage());
        }
    }

    @FXML
    private void handleBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);

        Stage stage = (Stage) browseButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        switchToAdminView(event);
    }

    private String copyPdfToResources(String sourceFilePath) {
        try {
            File sourceFile = new File(sourceFilePath);
            if (!sourceFile.exists()) {
                showErrorAlert("File Error", "The selected PDF file does not exist.");
                return null;
            }

            String destinationDir = "target/classes/pdf/";
            Path destinationPath = Paths.get(destinationDir);
            if (!Files.exists(destinationPath)) {
                Files.createDirectories(destinationPath);
            }

            String fileName = sourceFile.getName();
            Path destinationFilePath = Paths.get(destinationDir, fileName);
            Files.copy(sourceFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            return "/pdf/" + fileName;
        } catch (IOException e) {
            showErrorAlert("File Copy Error", "Error copying PDF file: " + e.getMessage());
            return null;
        }
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