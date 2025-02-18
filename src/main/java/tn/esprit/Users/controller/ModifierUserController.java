package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.services.ServiceDepartment;
import tn.esprit.Users.utils.Base;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ModifierUserController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<UserRole> roleBox;
    @FXML
    private TextField positionField;
    @FXML
    private TextField salaryField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<UserStatus> statusBox;
    @FXML
    private ComboBox<String> departmentBox;

    private final ServiceUsers serviceUsers = new ServiceUsers();
    private final ServiceDepartment serviceDepartment = new ServiceDepartment();
    private User selectedUser; // User being edited

    // Method to initialize fields with existing user data
    public void setUserData(User user) {
        this.selectedUser = user;

        // Pre-fill fields with existing user data
        nameField.setText(user.getIyedNomUser());
        emailField.setText(user.getIyedEmailUser());
        phoneField.setText(user.getIyedPhoneUser());
        roleBox.setValue(user.getIyedRoleUser());
        positionField.setText(user.getIyedPositionUser());
        salaryField.setText(String.valueOf(user.getIyedSalaireUser()));

        // Fix for Date conversion:
        // Convert java.util.Date to java.sql.Date if needed and set it to the DatePicker
        java.util.Date utilDate = user.getIyedDateEmbaucheUser();
        if (utilDate != null) {
            // Convert java.util.Date to java.sql.Date and then to LocalDate for DatePicker
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            datePicker.setValue(sqlDate.toLocalDate());
        }

        statusBox.setValue(user.getIyedStatutUser());

        // Populate departments
        departmentBox.getItems().clear(); // Clear any existing items
        if (!departmentBox.getItems().contains("No Department")) {
            departmentBox.getItems().add("No Department"); // Add "No Department" only if not already in the list
        }
        departmentBox.setValue("No Department");  // Default selection

        for (String departmentName : serviceDepartment.getDepartmentNames()) {
            departmentBox.getItems().add(departmentName);
            if (user.getIyedDepartment() != null &&
                    departmentName.equals(user.getIyedDepartment().getIyedNomDep())) {
                departmentBox.setValue(departmentName);
            }
        }
    }







    @FXML
    private void initialize() {
        roleBox.getItems().setAll(UserRole.values());
        statusBox.getItems().setAll(UserStatus.values());

        // Fetch department names dynamically
        List<String> departmentNames = serviceDepartment.getDepartmentNames();
        departmentBox.getItems().addAll(departmentNames);
    }

    @FXML
    private void handleSaveChanges() {
        if (selectedUser == null) {
            System.out.println("No user selected for modification.");
            return;
        }

        try {
            // Update user data from form fields
            selectedUser.setIyedNomUser(nameField.getText());
            selectedUser.setIyedEmailUser(emailField.getText());
            selectedUser.setIyedPhoneUser(phoneField.getText());
            selectedUser.setIyedRoleUser(roleBox.getValue());
            selectedUser.setIyedPositionUser(positionField.getText());
            selectedUser.setIyedSalaireUser(Double.parseDouble(salaryField.getText()));
            selectedUser.setIyedDateEmbaucheUser(Date.valueOf(datePicker.getValue()));
            selectedUser.setIyedStatutUser(statusBox.getValue());

            // Get selected department
            String departmentName = departmentBox.getValue();
            if (departmentName != null && !departmentName.equals("No Department")) {
                int departmentId = serviceDepartment.getDepartmentIdByName(departmentName);
                if (departmentId != 0) {
                    selectedUser.getIyedDepartment().setIyedIdDep(departmentId);
                }
            } else {
                selectedUser.setIyedDepartment(null);
            }

            // Update user in the database
            serviceUsers.modifier(selectedUser);
            System.out.println("User updated successfully!");

            // Close the window
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update user.");
        } catch (Exception e) {
            showAlert("Error", "Invalid input. Please check all fields.");
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    // Show alert message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
