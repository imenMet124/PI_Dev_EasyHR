package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;

import java.io.IOException;

public class DepartmentListCell extends ListCell<Department> {

    @FXML private VBox vbox;

    @FXML private Label lblName;
    @FXML private Label lblDescription;
    @FXML private Label lblLocation;
    @FXML private Label lblManager;

    public DepartmentListCell() {
        // Load the FXML for the custom ListCell
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DepartmentListItem.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Department department, boolean empty) {
        super.updateItem(department, empty);

        if (empty || department == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Set department details

            lblName.setText(department.getIyedNomDep());
            lblDescription.setText(department.getIyedDescriptionDep());
            lblLocation.setText(department.getIyedLocationDep());
            User manager = department.getIyedManager();
            lblManager.setText(manager != null ? manager.getIyedNomUser() : "No Manager");

            setText(null);  // No need for default text
            setGraphic(vbox); // Set the custom cell graphic
        }
    }
}
