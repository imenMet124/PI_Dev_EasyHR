package tn.esprit.Users.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;

import java.io.IOException;

public class SceneController {

    private static BorderPane mainPane; // Reference to mainPane in MainMenu

    // Set mainPane from MainMenuController
    public static void setMainPane(BorderPane pane) {
        mainPane = pane;
    }

    // Load an FXML page into the center of mainPane
    private static void loadPage(String fxmlPath) {
        if (mainPane == null) {
            System.out.println("MainPane not set in SceneController!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
            Parent newView = loader.load();
            mainPane.setCenter(newView); // Load content into the center
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for specifically loading ModifierDepartment page with data
    private static void loadPageForModifier(String fxmlPath, Department department) {
        if (mainPane == null) {
            System.out.println("MainPane not set in SceneController!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
            Parent newView = loader.load();

            // Pass the department data to the controller
            ModifierDepartmentController controller = loader.getController();
            controller.setDepartmentData(department);

            mainPane.setCenter(newView); // Load content into the center of mainPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load an FXML page into the center of mainPane and pass user data to the controller
    private static void loadUserPage(String fxmlPath, User user) {
        if (mainPane == null) {
            System.out.println("MainPane not set in SceneController!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
            Parent newView = loader.load();

            // Pass user data to the controller if it's not null
            if (user != null) {
                if (user instanceof User) {
                    ModifierUserController controller = loader.getController();
                    controller.setUserData(user); // Pass user data
                }
            }

            mainPane.setCenter(newView); // Load content into the center
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methods to load specific pages inside mainPane
    public static void openAjouterUserScene() {
        loadPage("/AjouterUser.fxml");
    }

    public static void openAfficherUsersScene() {
        loadPage("/AfficherUsers.fxml");
    }

    public static void openAjouterDepartmentScene() {
        loadPage("/AjouterDepartment.fxml");
    }

    public static void openAfficherDepartmentsScene() {
        loadPage("/AfficherDepartments.fxml");
    }

    // Method to open the ModifierUser page
    public static void openModifierUserScene(User user) {
        loadUserPage("/ModifierUser.fxml", user); // Load the page and pass the user data
    }

    public static void openModifierDepartmentScene(Department department) {
        loadPageForModifier("/ModifierDepartment.fxml", department);
    }

    // Method to open the Main Menu scene
    public static void openMainMenuScene() {
        loadPage("/MainMenu.fxml"); // Main menu does not require any data, so pass null
    }

    // Method to open the User Profile scene

    public static void openUserProfileScene() {
        loadPage("/UserProfile.fxml"); // Load the profile page
    }
    public static void openLoginScene() {
        try {
            // Load the Login.fxml file
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = new Stage(); // Create a new stage for the login scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Login.fxml: " + e.getMessage());
        }
    }
    public static void openVerificationCodeScene(String verificationCode) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/ClockInOut.fxml"));
            Parent root = loader.load();
            VerificationCodeController controller = loader.getController();
            controller.setVerificationCode(verificationCode); // Pass the verification code
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openNewPasswordScene() {
        try {
            Parent root = FXMLLoader.load(SceneController.class.getResource("/NewPassword.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openClockInOutScene() {
        loadPage("/ClockInOut.fxml"); // Load the Clock In/Out page
    }

}