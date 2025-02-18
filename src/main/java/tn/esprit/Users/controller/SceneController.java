package tn.esprit.Users.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.Users.entities.Department;

import java.io.IOException;

public class SceneController {
    private static Stage primaryStage;

    // Set the primary stage from the MainFX class
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Method to load the "Ajouter User" scene
    public static void loadAddUserScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/AjouterUser.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Add User");
    }

    // Method to load the "Afficher Users" scene
    public static void loadAfficherUserScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/AfficherUsers.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Afficher Users");
    }
    public static void loadAjouterDepartmentScene() {
        try {
            Parent root = FXMLLoader.load(SceneController.class.getResource("/AjouterDepartment.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadAfficherDepartmentsScene() {
        try {
            Parent root = FXMLLoader.load(SceneController.class.getResource("/AfficherDepartments.fxml"));
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
