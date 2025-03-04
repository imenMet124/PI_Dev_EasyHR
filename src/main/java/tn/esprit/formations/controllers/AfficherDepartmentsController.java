package tn.esprit.formations.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.formations.entities.Department;
import tn.esprit.Users.services.ServiceDepartment;

import java.sql.SQLException;
import java.util.List;

public class AfficherDepartmentsController {

    @FXML
    private ListView<Department> departmentListView; // Change to ListView

    @FXML
    private TextField searchField; // For the search functionality

    private ServiceDepartment serviceDepartment;

    // Initialize the ServiceDepartment in the constructor
    public AfficherDepartmentsController() {
        serviceDepartment = new ServiceDepartment(); // Initialize the department service
    }

    @FXML
    public void initialize() {
        // Initialize the ListView with department data
        departmentListView.setItems(getDepartments()); // Method to get departments

        // Set a custom cell factory to display department information
        departmentListView.setCellFactory(param -> new DepartmentListCell());
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
            departmentListView.setItems(filteredDepartments);
        } else {
            departmentListView.setItems(getDepartments());
        }
    }

    @FXML
    private void handleAdd() {
        SceneController.openAjouterDepartmentScene(); // Open in a new window
    }

    @FXML
    private void handleEdit() {
        Department selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            // Pass the selected department to the SceneController to load ModifierDepartment
            SceneController.openModifierDepartmentScene(selectedDepartment);
        } else {
            showAlert("No department selected", "Please select a department to edit.");
        }
    }



    @FXML
    private void handleDelete() {
        Department selectedDepartment = departmentListView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this department?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        serviceDepartment.supprimer(selectedDepartment.getIyedIdDep()); // Delete the department
                        departmentListView.setItems(getDepartments()); // Refresh the ListView
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

    // Custom ListCell to display department details
    private class DepartmentListCell extends javafx.scene.control.ListCell<Department> {
        @Override
        protected void updateItem(Department department, boolean empty) {
            super.updateItem(department, empty);

            if (empty || department == null) {
                setText(null);
            } else {
                // Set department details in the cell without the ID
                String departmentInfo = "Name: " + department.getIyedNomDep() + "\n" +
                        "Description: " + department.getIyedDescriptionDep() + "\n" +
                        "Location: " + department.getIyedLocationDep() + "\n" +
                        "Manager: " + (department.getIyedManager() != null ? department.getIyedManager().getIyedNomUser() : "No Manager");

                setText(departmentInfo);
            }
        }
    }

}
