package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.model.UserSession;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Hyperlink forgotPasswordLink; // Add this line

    private final ServiceUsers serviceUsers = new ServiceUsers();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }

        try {
            User user = serviceUsers.login(email, password);  // Call the login method from ServiceUsers
            if (user != null) {
                // Set the logged-in user in the UserSession
                UserSession.getInstance().setLoggedInUser(user);

                System.out.println("✅ Login successful! Welcome, " + user.getIyedNomUser());

                // Load the MainMenu.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
                Parent root = loader.load();

                // Get the current stage (window)
                Stage stage = (Stage) emailField.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Main Menu");
                stage.show();

                // Now that MainMenu.fxml is loaded, the mainPane should be set in MainMenuController
                MainMenuController mainMenuController = loader.getController();
                mainMenuController.initialize(); // Ensure mainPane is set
            } else {
                errorLabel.setText("❌ Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠ An error occurred while logging in.");
        }
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("Forgot Password clicked!");

        try {
            // Load the ForgotPassword.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgotPassword.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) forgotPasswordLink.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Forgot Password");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠ An error occurred while loading the forgot password page.");
        }
    }
}