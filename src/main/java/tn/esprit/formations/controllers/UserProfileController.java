package tn.esprit.formations.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.formations.entities.User;
import tn.esprit.Users.model.UserSession;

public class UserProfileController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblPhone;

    @FXML
    private Label lblRole;

    @FXML
    private Label lblPosition;

    @FXML
    private Label lblSalaire;

    @FXML
    private Label lblDateEmbauche;

    @FXML
    private Label lblStatut;

    @FXML
    private Label lblDepartment;

    @FXML
    public void initialize() {
        User loggedInUser = UserSession.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            lblName.setText(loggedInUser.getIyedNomUser());
            lblEmail.setText(loggedInUser.getIyedEmailUser());
            lblPhone.setText(loggedInUser.getIyedPhoneUser());
            lblRole.setText(loggedInUser.getIyedRoleUser().toString());
            lblPosition.setText(loggedInUser.getIyedPositionUser());
            lblSalaire.setText(String.valueOf(loggedInUser.getIyedSalaireUser()));
            lblDateEmbauche.setText(loggedInUser.getIyedDateEmbaucheUser().toString());
            lblStatut.setText(loggedInUser.getIyedStatutUser().toString());
            lblDepartment.setText(loggedInUser.getIyedDepartment() != null ?
                    loggedInUser.getIyedDepartment().getIyedNomDep() : "No Department");
        }
    }

    @FXML
    private void handleChangePassword() {
        // You can redirect the user to a password change page
        System.out.println("Redirecting to change password page...");
    }
}
