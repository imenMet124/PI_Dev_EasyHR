package tn.esprit.Users.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
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
}