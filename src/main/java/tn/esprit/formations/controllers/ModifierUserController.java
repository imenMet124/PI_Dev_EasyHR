package tn.esprit.formations.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.formations.entities.User;
import tn.esprit.formations.entities.UserRole;
import tn.esprit.formations.entities.UserStatus;
import tn.esprit.Users.services.ServiceDepartment;
import tn.esprit.Users.services.ServiceUsers;

import java.sql.Date;
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
        java.util.Date utilDate = user.getIyedDateEmbaucheUser();
        if (utilDate != null) {
            Date sqlDate = new Date(utilDate.getTime());
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

        // Input Validation
        if (!validateInputs()) {
            return;  // If validation fails, exit method
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

            // Show success message
            showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);

            // Optionally, you can leave the window open or close it as needed
            // Stage stage = (Stage) nameField.getScene().getWindow();
            // stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update user.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Invalid input. Please check all fields.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    // Input validation for all fields
    private boolean validateInputs() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        UserRole role = roleBox.getValue();
        String position = positionField.getText().trim();
        String salaryText = salaryField.getText().trim();
        UserStatus status = statusBox.getValue();
        String departmentName = departmentBox.getValue();

        // Validate name
        if (name.isEmpty()) {
            showAlert("Input Error", "Name cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }

        if (email.isEmpty()) {
            showAlert("Input Error", "Email cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }
        if (!isValidEmail(email)) {
            showAlert("Input Error", "Invalid email format.", Alert.AlertType.ERROR);
            return false;
        }

        if (phone.isEmpty()) {
            showAlert("Input Error", "Phone cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }

        if (role == null) {
            showAlert("Input Error", "Role must be selected.", Alert.AlertType.ERROR);
            return false;
        }
        if (position.isEmpty()) {
            showAlert("Input Error", "Position cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }
        if (salaryText.isEmpty()) {
            showAlert("Input Error", "Salary cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Double.parseDouble(salaryText);  // Validate salary
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Salary must be a valid number.", Alert.AlertType.ERROR);
            return false;
        }
        if (status == null) {
            showAlert("Input Error", "Status must be selected.", Alert.AlertType.ERROR);
            return false;
        }

        if (departmentName == null || departmentName.isEmpty()) {
            showAlert("Input Error", "Department must be selected.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
