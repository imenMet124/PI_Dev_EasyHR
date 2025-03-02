package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VerificationCodeController {

    private String verificationCode; // Store the verification code sent to the user

    @FXML
    private TextField codeField; // Field for verification code

    @FXML
    private Label errorLabel;

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @FXML
    private void handleVerifyCode() {
        String enteredCode = codeField.getText().trim();

        if (enteredCode.isEmpty()) {
            errorLabel.setText("Please enter the verification code.");
            return;
        }

        if (enteredCode.equals(verificationCode)) {
            // Code is correct, navigate to the new password page
            SceneController.openNewPasswordScene();
            Stage stage = (Stage) codeField.getScene().getWindow();
            stage.close(); // Close the current window
        } else {
            errorLabel.setText("Invalid verification code. Please try again.");
        }
    }
}