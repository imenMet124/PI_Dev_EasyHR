package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.Base;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private final ServiceUsers serviceUsers = new ServiceUsers();

    @FXML
    private void handleSignUp() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim(); // Get the text of the confirm password field

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return; // Stop the process if passwords don't match
        }

        // Check if the email is already taken (you can implement this check if needed)
        if (serviceUsers.isEmailTaken(email)) {
            errorLabel.setText("Email is already taken.");
            return;
        }

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("🔑 Hashed password before inserting into DB: " + hashedPassword);

        // Create a new User object (password will now be hashed)
        User newUser = new User(0, name, email, phone, hashedPassword, UserRole.EMPLOYE, "New Position", 3000.00, null, UserStatus.ACTIVE, null);

        try {
            serviceUsers.ajouter(newUser);  // Add the user to the database
            successLabel.setText("User signed up successfully!");
        } catch (SQLException e) {
            errorLabel.setText("Error during sign up: " + e.getMessage());
        }
    }



    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
