package tn.esprit.formations.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.formations.entities.UserRole;
import tn.esprit.formations.entities.UserStatus;
import tn.esprit.Users.services.ServiceDepartment;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.Base;

import java.io.IOException;
import java.sql.*;

public class AjouterUserController {

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
    private final ServiceDepartment serviceDepartment = new ServiceDepartment(); // Create instance of ServiceDepartment

    @FXML
    private void initialize() {
        // Populate combo boxes with enum values
        roleBox.getItems().setAll(UserRole.values());
        statusBox.getItems().setAll(UserStatus.values());
        departmentBox.getItems().add("No Department"); // Placeholder

        try (Connection connection = Base.getInstance().getConnection()) {
            Statement statement = connection.createStatement();

            // Fetch department names from the department table
            ResultSet resultSet = statement.executeQuery("SELECT iyedNomDep FROM department");

            while (resultSet.next()) {
                String departmentName = resultSet.getString("iyedNomDep");

                // Add department names to the ComboBox
                departmentBox.getItems().add(departmentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddUser() {
        if (!validateUserInputs()) return;
        PreparedStatement pst = null; // Declare pst outside try

        try {
            // Get values from form fields
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            UserRole role = roleBox.getValue();
            String position = positionField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            Date hireDate = Date.valueOf(datePicker.getValue());
            UserStatus status = statusBox.getValue();
            String departmentName = departmentBox.getValue();
            // Get department name
            System.out.println("Department name: " + departmentName);  // Log department name

            if (departmentName != null && !departmentName.equals("No Department")) {
                int departmentIdFromService = serviceDepartment.getDepartmentIdByName(departmentName);
                System.out.println("Department ID: " + departmentIdFromService);
            }

            // Static department ID for testing purposes
            //Integer departmentId = 11;  // Hardcoded static department ID

            // If you were testing dynamic fetching, this would be the place to use the dynamic method
             Integer departmentId = null;
             if (departmentName != null && !departmentName.equals("No Department")) {
                 int departmentIdFromService = serviceDepartment.getDepartmentIdByName(departmentName);
                 if (departmentIdFromService != 0) {
                     departmentId = departmentIdFromService;
                 }
             }

            // Get database connection
            Connection conn = Base.getInstance().getConnection();

            // SQL Query (matching your table column names)
            String query = "INSERT INTO user (iyedNomUser, iyedEmailUser, iyedPhoneUser, iyedRoleUser, iyedPositionUser, iyedSalaireUser, iyedDateEmbaucheUser, iyedStatutUser, iyedIdDepUser) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare and execute statement
            pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, role.toString());
            pst.setString(5, position);
            pst.setDouble(6, salary);
            pst.setDate(7, hireDate);
            pst.setString(8, status.toString());

            if (departmentId != null) {
                pst.setInt(9, departmentId);
            } else {
                pst.setNull(9, java.sql.Types.INTEGER);
            }

            // Execute the query
            pst.executeUpdate();

            System.out.println("User added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ignored) {
                }
            }
        }

    }
    @FXML
    private void handleBackToAfficherUser() {
        try {
            // Load the AfficherUser FXML file
            Parent root = FXMLLoader.load(getClass().getResource("AfficherUser.fxml"));

            // Get the current stage and set the new scene
            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally show an alert for errors
        }
    }

    private boolean validateUserInputs() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String salaryText = salaryField.getText().trim();

        // Check empty fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || salaryText.isEmpty()) {
            showAlert("Invalid Input", "All fields must be filled!");
            return false;
        }

        // Email validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert("Invalid Email", "Please enter a valid email address!");
            return false;
        }

        // Phone number validation (only digits, 8-15 chars)
        if (!phone.matches("\\d{8,15}")) {
            showAlert("Invalid Phone Number", "Phone number must be 8-15 digits long!");
            return false;
        }

        // Salary validation (numeric and non-negative)
        try {
            double salary = Double.parseDouble(salaryText);
            if (salary < 0) {
                showAlert("Invalid Salary", "Salary cannot be negative!");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Salary", "Salary must be a valid number!");
            return false;
        }

        // Date validation (cannot be in the future)
        if (datePicker.getValue() != null && datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            showAlert("Invalid Date", "Hire date cannot be in the future!");
            return false;
        }

        return true; // ✅ All validations passed
    }

    // ✅ Alert function
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}