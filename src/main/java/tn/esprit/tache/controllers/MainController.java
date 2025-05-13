package tn.esprit.tache.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.model.UserSession;
import tn.esprit.tache.services.TodoistService;

import java.io.IOException;
import java.util.Optional;

public class MainController {

    @FXML private BorderPane mainPane;
    @FXML private ImageView logo; // ✅ Logo ImageView
    @FXML private Button projetButton; // ✅ Projets Button
    @FXML private Button tacheButton;  // ✅ Tâches Button
    User loggedInUser = UserSession.getInstance().getLoggedInUser();



    @FXML
    public void initialize() {

         boolean isEmployee; // You can replace this with an actual check based on your app's user session.
        if (loggedInUser != null && (loggedInUser.getIyedRoleUser() == UserRole.RESPONSABLE_RH ||
                loggedInUser.getIyedRoleUser() == UserRole.CHEF_PROJET)) { isEmployee = false; }
        else { isEmployee = true; }
        // ✅ Load logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/images/logoEasyHR.png"));
        logo.setImage(logoImage);

        // Simulate checking the user's role (employee in this case)
        // Replace with actual session management or user role checking logic
        // You can replace this method to get the actual user role.

        // If the user is an employee, hide Projets and Tâches buttons
        if (isEmployee) {
            projetButton.setVisible(false);
            tacheButton.setVisible(false);
        }else {
            // If not an employee, ensure the buttons are visible
            projetButton.setVisible(true);
            tacheButton.setVisible(true);
        }

        openAffectationView();
    }

    @FXML
    private void openEmployeView() {
        loadView("/views/employe.fxml");
    }

    @FXML
    private void openProjetView() {
        loadView("/views/projet.fxml");
    }

    @FXML
    private void openTacheView() {
        loadView("/views/tache.fxml");
    }

    @FXML
    private void openAffectationView() {
        loadView("/views/affectation.fxml");
    }
    @FXML
    private void openTodoistView() {
        loadView("/views/todoist_view.fxml");
    }

    @FXML
    private void weatherView() {
        loadView("/views/weather.fxml");
    }

    @FXML
    private void quizView() {
        loadView("/views/quiz.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openChatBot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chatbot.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Chatbot OpenAI");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openChatBott() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/chattbot.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Chatbot Haggingface");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Handle Logout Action
    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to confirm logout.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ✅ Close Application Window
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.close();
        }
    }

    // Simulate checking if the user is an employee
    private boolean checkIfEmployee() {
        // Replace with actual logic to check if the logged-in user is an employee.
        // For example, you can check the user's role stored in the session or user management system.
        return true; // Assuming it's an employee for now.
    }
}
