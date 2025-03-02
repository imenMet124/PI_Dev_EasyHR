package tn.esprit.Users.controller;

import com.twilio.Twilio;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.SharedData;
import java.io.InputStream;
import java.util.Properties;

import java.sql.SQLException;

public class ForgotPasswordController {

    // Load Twilio credentials from a configuration file
    public static final String ACCOUNT_SID;
    public static final String AUTH_TOKEN;

    static {
        Properties props = new Properties();
        try (InputStream input = ForgotPasswordController.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (Exception e) {
            System.err.println("Failed to load Twilio credentials: " + e.getMessage());
        }
        ACCOUNT_SID = props.getProperty("twilio.account_sid");
        AUTH_TOKEN = props.getProperty("twilio.auth_token");
    }
    private static final String TWILIO_PHONE_NUMBER = "+17373672084"; // Ensure E.164 format

    static {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN); // Initialize Twilio
        } catch (Exception e) {
            System.err.println("Failed to initialize Twilio: " + e.getMessage());
        }
    }

    @FXML
    private TextField phoneField; // Field for phone number

    @FXML
    private Label errorLabel;

    @FXML
    private void handleSendSMS() {
        String phoneNumber = phoneField.getText().trim();

        if (phoneNumber.isEmpty()) {
            errorLabel.setText("Please enter your phone number.");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            errorLabel.setText("Invalid phone number format. Please use E.164 format (e.g., +1234567890).");
            return;
        }

        // Query the database to get the user by phone number
        ServiceUsers userService = new ServiceUsers();
        User user;
        try {
            user = userService.getUserByPhoneNumber(phoneNumber);
        } catch (SQLException e) {
            errorLabel.setText("Database error. Please try again.");
            e.printStackTrace();
            return;
        }

        if (user == null) {
            errorLabel.setText("No user found with this phone number.");
            return;
        }

        // Store the phone number in shared context
        SharedData.setPhoneNumber(phoneNumber);

        // Send SMS with verification code and user's name
        String verificationCode = generateVerificationCode();
        String smsMessage = String.format("Hello %s, this is your EasyHR account verification code: %s", user.getIyedNomUser(), verificationCode);
        boolean smsSent = sendSMS(phoneNumber, smsMessage);

        if (smsSent) {
            errorLabel.setText("An SMS with a verification code has been sent to your phone.");

            // Close the Forgot Password window
            Stage stage = (Stage) phoneField.getScene().getWindow();
            stage.close();

            // Open the Verification Code window
            SceneController.openVerificationCodeScene(verificationCode);
        } else {
            errorLabel.setText("Failed to send SMS. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        System.out.println("Navigating back to login...");

        // Open the Login page
        SceneController.openLoginScene();

        // Close the current (Forgot Password) window
        Stage stage = (Stage) phoneField.getScene().getWindow(); // Get the current stage
        stage.close(); // Close the stage
    }

    // Helper method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Validate that the phone number is in E.164 format (e.g., +1234567890)
        return phoneNumber.matches("^\\+\\d{10,15}$");
    }

    // Helper method to generate a random verification code
    private String generateVerificationCode() {
        // Generate a 6-digit random code
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    // Method to send an SMS using Twilio
    private boolean sendSMS(String phoneNumber, String message) {
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber; // Prepend '+' for E.164 format
        }
        try {
            Message.creator(
                    new PhoneNumber(phoneNumber), // Recipient's phone number
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // Your Twilio phone number (sender)
                    message // Message body
            ).create();
            return true; // SMS sent successfully
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to send SMS: " + e.getMessage()); // Show detailed error
            return false; // Failed to send SMS
        }
    }
}