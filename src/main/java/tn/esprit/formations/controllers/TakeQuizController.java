package tn.esprit.formations.controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.formations.entities.Formation;
import tn.esprit.formations.entities.Option;
import tn.esprit.formations.entities.Question;
import tn.esprit.formations.utils.GMailer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TakeQuizController {

    @FXML
    private Label quizTitleLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private ListView<Option> optionsListView;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnBack;

    private Formation formation;
    private List<Question> questions;
    private int currentQuestionIndex;
    private List<Option> userAnswers;
    private GMailer gMailer;

    public TakeQuizController() {
        try {
            this.gMailer = new GMailer();
            System.out.println("GMailer initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize GMailer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
        this.quizTitleLabel.setText(formation.getQuiz().getTitle());
        this.questions = formation.getQuiz().getQuestions();
        this.currentQuestionIndex = 0;
        this.userAnswers = new ArrayList<>(questions.size());
        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add(null);
        }
        displayCurrentQuestion();
    }

    @FXML
    public void initialize() {
        optionsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        optionsListView.setCellFactory(param -> new ListCell<Option>() {
            @Override
            protected void updateItem(Option item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getText());
                }
            }
        });
    }

    private void displayCurrentQuestion() {
        if (questions == null || questions.isEmpty()) {
            questionLabel.setText("No questions available.");
            optionsListView.getItems().clear();
            btnNext.setDisable(true);
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText((currentQuestionIndex + 1) + ". " + currentQuestion.getText());
        optionsListView.getItems().setAll(currentQuestion.getOptions());

        Option previousAnswer = userAnswers.get(currentQuestionIndex);
        if (previousAnswer != null) {
            optionsListView.getSelectionModel().select(previousAnswer);
        } else {
            optionsListView.getSelectionModel().clearSelection();
        }

        btnNext.setText(currentQuestionIndex == questions.size() - 1 ? "Submit" : "Next");
        btnNext.setDisable(false);
        btnBack.setDisable(currentQuestionIndex == 0);
    }

    @FXML
    private void handleNext(ActionEvent event) {
        Option selectedOption = optionsListView.getSelectionModel().getSelectedItem();
        if (selectedOption == null) {
            showErrorAlert("No Selection", "Please select an option before proceeding.");
            return;
        }
        userAnswers.set(currentQuestionIndex, selectedOption);

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayCurrentQuestion();
        } else {
            int correctAnswers = 0;
            for (int i = 0; i < questions.size(); i++) {
                Option userAnswer = userAnswers.get(i);
                if (userAnswer != null && userAnswer.isCorrect()) {
                    correctAnswers++;
                }
            }

            float passingScore = formation.getQuiz().getPassingscore();
            boolean passed = correctAnswers >= passingScore;
            String resultMessage = passed
                    ? "Congratulations! You passed the " + formation.getQuiz().getTitle() + " quiz!\n"
                    : "You did not pass the " + formation.getQuiz().getTitle() + " quiz, but keep practicing!\n";
            resultMessage += "Your Score: " + correctAnswers + " out of " + questions.size() + "\n";
            resultMessage += "Passing Score: " + passingScore;

            // Show result in UI
            showInfoAlert("Quiz Completed", resultMessage);

            // Send email with quiz results and PDF certificate if passed
            try {
                String userEmail = "ahmed.mtimet@esprit.tn"; // Replace with actual user email
                String emailSubject = "Quiz Results: " + formation.getQuiz().getTitle();

                // Determine the status and additional message based on pass/fail
                String status = passed ? "Accepted" : "Rejected";
                String additionalMessage = passed
                        ? "Congratulations on passing the quiz! Please find your certificate of completion attached."
                        : "Unfortunately, you did not pass this time. Please keep practicing and try again!";

                // Create a detailed email message
                String emailMessage = """
                        Dear User,

                        We are pleased to inform you about the results of your recent quiz.

                        Quiz Title: %s
                        Your Score: %d out of %d
                        Passing Score: %.2f
                        Status: %s

                        %s

                        Best regards,
                        The Training Team
                        """.formatted(
                        formation.getQuiz().getTitle(),
                        correctAnswers,
                        questions.size(),
                        passingScore,
                        status,
                        additionalMessage
                );

                File pdfFile = null;
                if (passed) {
                    // Generate PDF certificate if the user passed
                    pdfFile = generateCertificate(formation.getTitre(), "Ahmed Mtimet"); // Static user name
                }

                if (gMailer != null) {
                    System.out.println("Attempting to send email to: " + userEmail);
                    System.out.println("Email subject: " + emailSubject);
                    System.out.println("Email content:\n" + emailMessage);
                    if (pdfFile != null) {
                        System.out.println("Attaching PDF: " + pdfFile.getAbsolutePath());
                    }
                    gMailer.sendMail(userEmail, emailSubject, emailMessage, pdfFile);
                    System.out.println("Email sent to user with quiz results.");

                    // Clean up: Delete the temporary PDF file after sending
                    if (pdfFile != null && pdfFile.exists()) {
                        pdfFile.delete();
                        System.out.println("Temporary PDF file deleted.");
                    }
                } else {
                    System.err.println("GMailer is not initialized. Cannot send email.");
                    showErrorAlert("Email Error", "Email service is not initialized. Please contact support.");
                }
            } catch (Exception e) {
                System.err.println("Failed to send email: " + e.getMessage());
                e.printStackTrace();
                showErrorAlert("Email Error", "Failed to send quiz results via email: " + e.getMessage());
            }

            switchToUserView(event);
        }
    }

    // Method to generate a PDF certificate
    private File generateCertificate(String formationName, String userName) throws IOException {
        String fileName = "certificate_" + formationName.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
        File pdfFile = new File(fileName);

        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Certificate of Completion")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.add(new Paragraph("\nThis certifies that\n")
                .setFontSize(14)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.add(new Paragraph(userName)
                .setFontSize(16)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.add(new Paragraph("\nhas successfully completed the\n")
                .setFontSize(14)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.add(new Paragraph(formationName)
                .setFontSize(16)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.add(new Paragraph("\nDate: " + java.time.LocalDate.now())
                .setFontSize(12)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        document.close();
        return pdfFile;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Option selectedOption = optionsListView.getSelectionModel().getSelectedItem();
        if (selectedOption != null) {
            userAnswers.set(currentQuestionIndex, selectedOption);
        }

        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayCurrentQuestion();
        }
    }

    private void switchToUserView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/UserView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) quizTitleLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not return to User View: " + e.getMessage());
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