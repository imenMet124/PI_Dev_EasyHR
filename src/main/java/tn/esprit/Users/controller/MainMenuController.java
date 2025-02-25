package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainMenuController {

    @FXML
    private BorderPane mainPane; // Reference from FXML

    @FXML
    public void initialize() {
        SceneController.setMainPane(mainPane); // Link mainPane to SceneController
    }

    @FXML
    private void goToAjouterUser() {
        SceneController.openAjouterUserScene();
    }

    @FXML
    private void goToAfficherUsers() {
        SceneController.openAfficherUsersScene();
    }

    @FXML
    private void goToAjouterDepartment() {
        SceneController.openAjouterDepartmentScene();
    }

    @FXML
    private void goToAfficherDepartments() {
        SceneController.openAfficherDepartmentsScene();
    }





    public void handleLogout() {
        System.out.println("Logout clicked!");
    }
}
