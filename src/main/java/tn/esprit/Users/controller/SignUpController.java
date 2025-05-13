package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import org.json.JSONArray;
//import org.json.JSONObject;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.KairosAPI;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import tn.esprit.Users.services.UserPhotoService;



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

    @FXML
    private ImageView webcamView;

    private Webcam webcam;
    private File capturedImageFile;

    private final ServiceUsers serviceUsers = new ServiceUsers();
    private final UserPhotoService userPhotoService = new UserPhotoService();
    @FXML
    private void initialize() {
        // Initialize the webcam
        startWebcam();
    }

    private void startWebcam() {
        webcam = Webcam.getDefault();

        if (webcam == null) {
            errorLabel.setText("No webcam detected.");
            return;
        }

        // Set resolution to at least 640x480 to meet Kairos API requirements
        webcam.setViewSize(new Dimension(640, 480));

        if (webcam.isOpen()) {
            errorLabel.setText("Webcam is already in use. Attempting to restart...");
            try {
                webcam.close();
                errorLabel.setText("Webcam released. Reopening...");
            } catch (Exception e) {
                errorLabel.setText("Failed to release webcam: " + e.getMessage());
                return;
            }
        }

        try {
            webcam.open();

            WebcamPanel webcamPanel = new WebcamPanel(webcam);
            webcamPanel.setFPSDisplayed(true);
            webcamPanel.setDisplayDebugInfo(true);

            SwingNode swingNode = new SwingNode();
            createSwingContent(swingNode, webcamPanel);

            VBox parent = (VBox) webcamView.getParent();
            parent.getChildren().remove(webcamView);
            parent.getChildren().add(swingNode);

            errorLabel.setText("Webcam started successfully.");
        } catch (Exception e) {
            errorLabel.setText("Error initializing webcam: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSwingContent(SwingNode swingNode, WebcamPanel webcamPanel) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(webcamPanel));
    }

    public void releaseWebcam() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    @FXML
    private void handleCaptureImage() {
        if (webcam != null) {
            // Capture the image from the webcam
            BufferedImage bufferedImage = webcam.getImage();
            if (bufferedImage != null) {
                try {
                    // Save the image to a file
                    capturedImageFile = new File("captured_image.jpg");
                    ImageIO.write(bufferedImage, "jpg", capturedImageFile);
                    successLabel.setText("Image captured successfully!");
                } catch (IOException e) {
                    errorLabel.setText("Error capturing image: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleSignUp() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields must be filled!");
            return;
        }

        // Email validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            errorLabel.setText("Invalid email format!");
            return;
        }

        // Phone number validation (8-15 digits)
        if (!phone.matches("\\d{8,15}")) {
            errorLabel.setText("Phone number must be 8-15 digits long!");
            return;
        }

        // Password length check
        if (password.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters long!");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match!");
            return;
        }

        // Check if email is already taken
        if (serviceUsers.isEmailTaken(email)) {
            errorLabel.setText("Email is already taken!");
            return;
        }

        // Check if an image was captured
        if (capturedImageFile == null) {
            errorLabel.setText("Please capture an image before signing up.");
            return;
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create user object
        User newUser = new User(0, name, email, phone, hashedPassword, UserRole.EMPLOYE, "New Position", 3000.00, null, UserStatus.ACTIVE, null);

        try {
            // Add user to database
            serviceUsers.ajouter(newUser);
            int userId = serviceUsers.getLastInsertedUserId();
            if (userId <= 0) {
                errorLabel.setText("Error retrieving user ID.");
                return;
            }

            // Save image
            String imagePath = "src/main/resources/images/users/" + userId + ".jpg";
            File destinationFile = new File(imagePath);
            ImageIO.write(ImageIO.read(capturedImageFile), "jpg", destinationFile);

            // Store image path in the database
            userPhotoService.addUserPhoto(userId, imagePath);

            successLabel.setText("User signed up successfully!");
            releaseWebcam();
        } catch (SQLException | IOException e) {
            errorLabel.setText("Error during sign-up: " + e.getMessage());
        }
    }



    // Helper method to check if the enrollment was successful
//    private boolean isEnrollmentSuccessful(String kairosResponse) {
//        try {
//            JSONObject jsonResponse = new JSONObject(kairosResponse);
//            // Check if the response contains an "images" array
//            if (jsonResponse.has("images")) {
//                JSONArray images = jsonResponse.getJSONArray("images");
//                if (images.length() > 0) {
//                    JSONObject firstImage = images.getJSONObject(0);
//                    // Check if the transaction status is "success"
//                    if (firstImage.has("transaction") && firstImage.getJSONObject("transaction").has("status")) {
//                        String status = firstImage.getJSONObject("transaction").getString("status");
//                        return "success".equalsIgnoreCase(status);
//                    }
//                }
//            }
//            // Check for errors in the response
//            if (jsonResponse.has("Errors")) {
//                JSONArray errors = jsonResponse.getJSONArray("Errors");
//                if (errors.length() > 0) {
//                    JSONObject firstError = errors.getJSONObject(0);
//                    System.err.println("Kairos Error: " + firstError.getString("Message"));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    @FXML
    private void handleLoginRedirect() {
        // Release the webcam if it's open
        releaseWebcam();

        // Get the current stage and close it
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();

        // Open the login scene
        SceneController.openLoginScene();
    }

}