package tn.esprit.formations.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.formations.entities.Department;
import tn.esprit.formations.entities.User;
import tn.esprit.Users.services.ServiceDepartment;
import tn.esprit.Users.services.ServiceUsers;

import java.sql.SQLException;
import java.util.List;

public class ModifierDepartmentController {

    @FXML
    private TextField departmentNameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<String> managerComboBox;

    private final ServiceDepartment serviceDepartment = new ServiceDepartment();
    private final ServiceUsers serviceUsers = new ServiceUsers();
    private Department currentDepartment; // Store the current department being edited

    // Method to initialize fields with existing department data
    public void setDepartmentData(Department department) {
        this.currentDepartment = department;

        // Pre-fill fields with existing department data
        departmentNameField.setText(department.getIyedNomDep());
        descriptionField.setText(department.getIyedDescriptionDep());
        locationField.setText(department.getIyedLocationDep());

        // Populate manager dropdown
        managerComboBox.getItems().clear();
        List<String> managerNames = serviceUsers.getUserNames();
        managerComboBox.getItems().addAll(managerNames);

        if (department.getIyedManager() != null) {
            managerComboBox.setValue(department.getIyedManager().getIyedNomUser());
        }
    }

    @FXML
    private void handleSaveChanges() {
        if (currentDepartment == null) {
            System.out.println("No department selected for modification.");
            return;
        }

        try {
            // Update department data from form fields
            currentDepartment.setIyedNomDep(departmentNameField.getText());
            currentDepartment.setIyedDescriptionDep(descriptionField.getText());
            currentDepartment.setIyedLocationDep(locationField.getText());

            // Get selected manager
            String selectedManagerName = managerComboBox.getValue();
            User newManager = serviceUsers.getUserByName(selectedManagerName);
            currentDepartment.setIyedManager(newManager);

            // Update department in the database
            serviceDepartment.modifier(currentDepartment);

            // Show success message instead of closing window
            showAlert("Success", "Department updated successfully!", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update department.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Invalid input. Please check all fields.", Alert.AlertType.ERROR);
        }
    }

    // Show alert message
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
