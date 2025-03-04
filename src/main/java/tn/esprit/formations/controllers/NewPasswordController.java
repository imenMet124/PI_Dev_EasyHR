package tn.esprit.formations.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.formations.utils.SharedData;

import java.sql.SQLException;

public class NewPasswordController {

    @FXML
    private TextField newPasswordField; // Field for new password

    @FXML
    private TextField confirmPasswordField; // Field for confirming new password

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel; // Label to display success message

    @FXML
    private Hyperlink backToLoginLink; // Hyperlink to go back to login

    @FXML
    private void initialize() {
        // Hide the success label and back-to-login link initially
        successLabel.setVisible(false);
        backToLoginLink.setVisible(false);
    }

    @FXML
    private void handleSetPassword() {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        // Retrieve the phone number from shared context
        String phoneNumber = SharedData.getPhoneNumber();

        if (phoneNumber == null) {
            errorLabel.setText("Session expired. Please restart the process.");
            return;
        }

        // Update the password in the database
        ServiceUsers userService = new ServiceUsers();
        boolean passwordUpdated;
        try {
            passwordUpdated = userService.updatePassword(phoneNumber, newPassword);
        } catch (SQLException e) {
            errorLabel.setText("Database error. Please try again.");
            e.printStackTrace();
            return;
        }

        if (passwordUpdated) {
            // Display success message
            successLabel.setText("Password updated successfully!");
            successLabel.setVisible(true);

            // Show the back-to-login link
            backToLoginLink.setVisible(true);

            // Clear the error message
            errorLabel.setText("");
        } else {
            errorLabel.setText("Failed to update password. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        // Navigate back to the login page
        SceneController.openLoginScene();

        // Close the current (New Password) window
        Stage stage = (Stage) newPasswordField.getScene().getWindow();
        stage.close();
    }
}