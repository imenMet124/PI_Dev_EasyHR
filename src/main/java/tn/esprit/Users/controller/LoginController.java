package tn.esprit.Users.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.model.UserSession;



public class LoginController {
    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Button togglePasswordButton;


    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField visiblePassword;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView eyeIcon;


    @FXML
    private Hyperlink forgotPasswordLink; // Add this line

    private final ServiceUsers serviceUsers = new ServiceUsers();


    public void initialize() {
        passwordField.textProperty().bindBidirectional(visiblePassword.textProperty());
        eyeIcon.setImage(closedEye);}

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

                // Load Acceuil.fxml instead of MainMenu.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Acceuil.fxml"));
                Parent root = loader.load();

                // Get the current stage (window)
                Stage stage = (Stage) emailField.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Acceuil");
                stage.show();
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
    @FXML
    private void handleSignUpRedirect() {
        System.out.println("Sign Up clicked!");

        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signUp.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) signUpLink.getScene().getWindow(); // Assuming your hyperlink has fx:id="signUpLink"

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("⚠ An error occurred while loading the sign-up page.");
        }
    }


    private boolean passwordVisible = false;
    private final Image openEye = new Image(getClass().getResourceAsStream("/images/eye-open.png"));
    private final Image closedEye = new Image(getClass().getResourceAsStream("/images/eye-closed.png"));
    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            // Show plain text
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visiblePassword.setVisible(true);
            visiblePassword.setManaged(true);
            eyeIcon.setImage(openEye);
        } else {
            // Hide plain text
            visiblePassword.setVisible(false);
            visiblePassword.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            eyeIcon.setImage(closedEye);
        }
    }
}