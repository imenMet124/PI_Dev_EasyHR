package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceUsers;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

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
                System.out.println("✅ Login successful! Welcome, " + user.getIyedNomUser());
                SceneController.openAfficherUsersScene(); // Navigate to main page after successful login
            } else {
                errorLabel.setText("❌ Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠ An error occurred while logging in.");
        }
    }
}
