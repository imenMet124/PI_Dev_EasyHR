package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.Base;
import tn.esprit.Users.entities.Department;
import tn.esprit.Users.services.ServiceDepartment;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

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


}