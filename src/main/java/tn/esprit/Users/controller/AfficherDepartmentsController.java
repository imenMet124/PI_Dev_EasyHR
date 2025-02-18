package tn.esprit.Users.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceDepartment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherDepartmentsController {

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Department, String> colId;
    @FXML
    private TableColumn<Department, String> colName;
    @FXML
    private TableColumn<Department, String> colDescription;
    @FXML
    private TableColumn<Department, String> colLocation;
    @FXML
    private TableColumn<Department, String> colManager;

    @FXML
    private TextField searchField; // For the search functionality

    private ServiceDepartment serviceDepartment;

    // Initialize the ServiceDepartment in the constructor
    public AfficherDepartmentsController() {
        serviceDepartment = new ServiceDepartment(); // Initialize the department service
    }

    @FXML
    public void initialize() {
        // Initialize the TableView with department data
        departmentTable.setItems(getDepartments()); // Method to get departments

        // Set up the TableColumns with data from the Department class
        colId.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getIyedIdDep())));
        colName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getIyedNomDep()));
        colDescription.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getIyedDescriptionDep()));
        colLocation.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getIyedLocationDep()));

        // Manager column uses a custom property to display manager name
        colManager.setCellValueFactory(param -> {
            User manager = param.getValue().getIyedManager();
            return new SimpleStringProperty(manager != null ? manager.getIyedNomUser() : "No Manager");
        });

    }

    // Method to get departments from the service
    private ObservableList<Department> getDepartments() {
        try {
            List<Department> departments = serviceDepartment.afficher();
            return FXCollections.observableArrayList(departments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList(); // Return an empty list if there's an error
    }

    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().toLowerCase();

        if (!searchQuery.isEmpty()) {
            ObservableList<Department> filteredDepartments = FXCollections.observableArrayList();
            try {
                List<Department> allDepartments = serviceDepartment.afficher();
                for (Department department : allDepartments) {
                    if (department.getIyedNomDep().toLowerCase().contains(searchQuery)) {
                        filteredDepartments.add(department);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            departmentTable.setItems(filteredDepartments);
        } else {
            departmentTable.setItems(getDepartments());
        }
    }

    @FXML
    private void handleAdd() {
        SceneController.loadAjouterDepartmentScene(); // Navigate to the "Add User" scene
    }

    @FXML
    private void handleEdit() {
        Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                // Load the ModifierDepartment.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDepartment.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected department data
                ModifierDepartmentController controller = loader.getController();
                controller.setDepartmentData(selectedDepartment);

                // Get the current stage and set the new scene
                Stage stage = (Stage) departmentTable.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No department selected", "Please select a department to edit.");
        }
    }



    @FXML
    private void handleDelete() {
        Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this department?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        serviceDepartment.supprimer(selectedDepartment.getIyedIdDep()); // Delete the department
                        departmentTable.setItems(getDepartments()); // Refresh the table
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("No department selected", "Please select a department to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
