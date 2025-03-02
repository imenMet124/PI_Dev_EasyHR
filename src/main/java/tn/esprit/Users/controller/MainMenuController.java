package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainMenuController {

    @FXML
    private BorderPane mainPane; // Reference from FXML

    @FXML
    public void initialize() {
        if (mainPane != null) {
            SceneController.setMainPane(mainPane); // Link mainPane to SceneController
        } else {
            System.err.println("mainPane is null in MainMenuController!");
        }
    }

    @FXML
    private void goToAjouterUser() {
        System.out.println("Navigating to Ajouter User...");
        SceneController.openAjouterUserScene();
    }

    @FXML
    private void goToAfficherUsers() {
        System.out.println("Navigating to Afficher Users...");
        SceneController.openAfficherUsersScene();
    }

    @FXML
    private void goToAjouterDepartment() {
        System.out.println("Navigating to Ajouter Department...");
        SceneController.openAjouterDepartmentScene();
    }

    @FXML
    private void goToAfficherDepartments() {
        System.out.println("Navigating to Afficher Departments...");
        SceneController.openAfficherDepartmentsScene();
    }

    @FXML
    private void goToUserProfile() {
        System.out.println("Navigating to User Profile...");
        SceneController.openUserProfileScene();
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logout clicked!");
        // Add logout logic here (e.g., clear session, return to login screen)
    }

    @FXML
    private void goToClockInOut() {
        System.out.println("Navigating to Clock In/Out...");
        SceneController.openClockInOutScene(); // Load the Clock In/Out page
    }
}