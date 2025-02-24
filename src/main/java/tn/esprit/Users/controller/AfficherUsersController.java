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
import java.sql.SQLException;
import java.util.List;

public class AfficherUsersController {

    @FXML
    private ListView<User> userListView;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private ServiceUsers serviceUsers = new ServiceUsers();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load users into the ListView
        try {
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Use the custom ListCell
        userListView.setCellFactory(param -> new UserListCell());
    }

    // Load users into the ListView
    private void loadUsers() throws SQLException {
        List<User> users = serviceUsers.afficher();
        userList.setAll(users);
        userListView.setItems(userList);
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
            userListView.setItems(filteredList);
        } else {
            userListView.setItems(userList);  // Show all users if no search text
        }
    }

    // Add user
    @FXML
    private void handleAdd() {
        SceneController.openAjouterUserScene(); // Open in a new window
    }

    // Edit user
    @FXML
    private void handleEdit() {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
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

    @FXML
    private void handleDelete() {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Implement delete logic (e.g., remove from database and refresh list)
            showAlert("User Deleted", "The user has been successfully deleted.");
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
