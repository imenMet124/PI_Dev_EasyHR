package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceDepartment;
import tn.esprit.Users.services.ServiceUsers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AjouterDepartmentController {

    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField locationField;
    @FXML private ComboBox<String> managerComboBox;

    private final ServiceDepartment serviceDepartment = new ServiceDepartment();
    private final ServiceUsers serviceUsers = new ServiceUsers();

    public void initialize() {
        try {
            // Fetch users from service
            List<User> users = serviceUsers.afficher(); // Assuming `afficher()` gets all users

            // Extract user names to populate the ComboBox
            List<String> userNames = new ArrayList<>();
            for (User user : users) {
                userNames.add(user.getIyedNomUser());  // Assuming `getIyedNomUser()` gets the user's name
            }

            // Add user names to the ComboBox
            managerComboBox.getItems().addAll(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSaveDepartment() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String location = locationField.getText().trim();
        String managerName = managerComboBox.getValue();

        if (name.isEmpty() || description.isEmpty() || location.isEmpty() || managerName == null) {
            showError("Input Error", "Please fill in all fields.");
            return;
        }

        try {
            // Get the manager ID based on the selected manager's name
            int managerId = serviceUsers.getUserIdByName(managerName); // Correct method to get the manager's ID
            User manager = serviceUsers.getById(managerId); // Fetch the User object for the manager

            // Create and save the new department
            Department department = new Department(0, "Department Name", "Description", "Location", new ArrayList<>(), null);

            department.setIyedNomDep(name);
            department.setIyedDescriptionDep(description);
            department.setIyedLocationDep(location);
            department.setIyedManager(manager);

            serviceDepartment.ajouter(department); // Save department

            // Show success message
            showSuccess("Department Added", "The department has been successfully added.");
        } catch (SQLException e) {
            showError("Error", "Failed to add the department.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack() {
        // Handle the back action (navigate to another page or close)
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
