package tn.esprit.Users.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static Stage primaryStage; // Main Menu Stage

    // Set the primary stage from MainFX (called once)
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Load the Main Menu (ONLY ONCE, never closed)
    public static void loadMainMenuScene() {
        try {
            Parent root = FXMLLoader.load(SceneController.class.getResource("/AfficherDepartments.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Main Menu");
            primaryStage.show(); // Ensure it stays open
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Open a new window for other pages
    private static void openNewWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(SceneController.class.getResource(fxmlPath));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methods to open other pages in new windows
    public static void openAjouterUserScene() {
        openNewWindow("/AjouterUser.fxml", "Ajouter User");
    }

    public static void openAfficherUsersScene() {
        openNewWindow("/AfficherUsers.fxml", "Afficher Users");
    }

    public static void openAjouterDepartmentScene() {
        openNewWindow("/AjouterDepartment.fxml", "Ajouter Department");
    }

    public static void openAfficherDepartmentsScene() {
        openNewWindow("/AfficherDepartments.fxml", "Afficher Departments");
    }

    public static void openModifierDepartmentScene() {
        openNewWindow("/ModifierDepartment.fxml", "Modifier Department");
    }

    public static void openModifierUserScene() {
        openNewWindow("/ModifierUser.fxml", "Modifier User");
    }
}
