package tn.esprit.Users.controller;

import javafx.fxml.FXML;

public class MainMenuController {

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

    @FXML
    private void goToModifierUser() {
        SceneController.openModifierUserScene();
    }

    @FXML
    private void goToModifierDepartment() {
        SceneController.openModifierDepartmentScene();
    }
}
