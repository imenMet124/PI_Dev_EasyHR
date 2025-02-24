package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListCell;
import tn.esprit.Users.entities.User;

import java.io.IOException;
import java.sql.Date;

public class UserListCell extends ListCell<User> {

    @FXML private VBox vbox;  // The VBox container from FXML
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Label lblRole;
    @FXML private Label lblPosition;
    @FXML private Label lblSalary;
    @FXML private Label lblStatus;
    @FXML private Label lblDepartment;
    @FXML private Label lblHireDate;

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Load the FXML file and inject the controller fields
            if (vbox == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserListItem.fxml"));
                loader.setController(this);  // Ensure controller is set properly
                try {
                    Parent root = loader.load();  // Load the FXML
                    vbox = (VBox) root;  // Get the VBox container from FXML
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Set user details to the labels
            lblName.setText(user.getIyedNomUser());
            lblEmail.setText(user.getIyedEmailUser());
            lblPhone.setText(user.getIyedPhoneUser() != null ? user.getIyedPhoneUser() : "N/A");
            lblRole.setText("Role: " + user.getIyedRoleUser().name());
            lblPosition.setText("Position: " + user.getIyedPositionUser());
            lblSalary.setText("Salary: $" + user.getIyedSalaireUser());
            lblStatus.setText("Status: " + user.getIyedStatutUser().name());
            lblDepartment.setText("Department: " +
                    (user.getIyedDepartment() != null ? user.getIyedDepartment().getIyedNomDep() : "No Department"));

            // Format hire date
            Date hireDate = (user.getIyedDateEmbaucheUser() != null)
                    ? new Date(user.getIyedDateEmbaucheUser().getTime()) : null;
            lblHireDate.setText(hireDate != null ? "Hired: " + hireDate.toString() : "Hired: N/A");

            // Update the list cell with the loaded FXML
            setText(null);
            setGraphic(vbox);
        }
    }
}
