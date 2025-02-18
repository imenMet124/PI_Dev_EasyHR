package tn.esprit.Users.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceUsers;

import java.io.IOException;
import java.sql.Date;
import javafx.beans.property.SimpleObjectProperty;


import java.sql.SQLException;
import java.util.List;

public class AfficherUsersController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colPhone;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, String> colPosition;
    @FXML
    private TableColumn<User, Double> colSalary;
    @FXML
    private TableColumn<User, String> colStatus;
    @FXML
    private TableColumn<User, String> colDepartment;
    @FXML
    private TableColumn<User, Date> colHireDate;  // Add this line
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAdd;
    private SceneController sceneController;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private ServiceUsers serviceUsers = new ServiceUsers();
    private ObservableList<User> userList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Bind table columns using getter methods
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIyedIdUser()).asObject());
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIyedNomUser()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIyedEmailUser()));
        colPhone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIyedPhoneUser()));
        colRole.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIyedRoleUser().name()));
        colPosition.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIyedPositionUser()));
        colSalary.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getIyedSalaireUser()).asObject());
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIyedStatutUser().name()));

        colHireDate.setCellValueFactory(cellData -> {
                    java.util.Date utilDate = cellData.getValue().getIyedDateEmbaucheUser();
                    if (utilDate != null) {
                        // Convert java.util.Date to java.sql.Date
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        return new javafx.beans.property.SimpleObjectProperty<>(sqlDate);
                    } else {
                        return null;
                    }
        });

        colDepartment.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getIyedDepartment() != null ? cellData.getValue().getIyedDepartment().getIyedNomDep() : "No Department"
        ));

        // Load data
        try {
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to load users into the TableView
    private void loadUsers() throws SQLException {
        List<User> users = serviceUsers.afficher();
        userList.setAll(users);
        userTable.setItems(userList);
    }

    // Search functionality
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            ObservableList<User> filteredList = FXCollections.observableArrayList();
            for (User user : userList) {
                if (user.getIyedNomUser().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(user);
                }
            }
            userTable.setItems(filteredList);
        } else {
            userTable.setItems(userList);  // Show all users if no search text
        }
    }

    // Add user
    @FXML
    private void handleAdd() {
        try {
            SceneController.loadAddUserScene(); // Navigate to the "Add User" scene
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Edit user
    @FXML
    private void handleEdit() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUser.fxml"));
                Parent root = loader.load();

                ModifierUserController controller = loader.getController();
                controller.setUserData(selectedUser); // Pass user data to edit form

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit User");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a user to edit.");
        }
    }


    // Delete user
    @FXML
    private void handleDelete() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                serviceUsers.supprimer(selectedUser.getIyedIdUser());
                userList.remove(selectedUser);  // Remove from the table
                showAlert("Success", "User deleted successfully.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete user.");
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a user to delete.");
        }
    }

    // Show alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
