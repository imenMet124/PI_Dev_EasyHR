package tn.esprit.tache.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Employe;
import tn.esprit.tache.entities.Departement;
import tn.esprit.tache.services.EmployeService;
import tn.esprit.tache.services.DepartementService;

import java.io.IOException;
import java.util.List;

public class EmployeController {

    @FXML private TableView<Employe> employeTable;
    @FXML private TableColumn<Employe, Integer> idCol;
    @FXML private TableColumn<Employe, String> nomCol;
    @FXML private TableColumn<Employe, String> emailCol;
    @FXML private TableColumn<Employe, String> roleCol;
    @FXML private TableColumn<Employe, String> departementCol;
    @FXML private Button addEmployeBtn, editEmployeBtn, deleteEmployeBtn;

    private final EmployeService employeService = new EmployeService();
    private final DepartementService departementService = new DepartementService();
    private ObservableList<Employe> employeList;

    @FXML
    public void initialize() {
        // ✅ Bind TableView columns to Employe attributes
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdEmp()));
        nomCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomEmp()));
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        roleCol.setCellValueFactory(cellData -> {
            String role = cellData.getValue().getRole();
            String emoji;

            switch (role) {
                case "Manager":
                    emoji = "👨‍💼 ";
                    break;
                case "Développeur":
                    emoji = "💻 ";
                    break;
                case "Admin":
                    emoji = "🔧 ";
                    break;
                default:
                    emoji = "";
            }

            return new SimpleStringProperty(emoji + role);
        });
        departementCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartement().getNomDep()));

        // ✅ Load Employees into TableView
        loadEmployes();
    }

    private void loadEmployes() {
        List<Employe> employes = employeService.getAllEmployes();
        employeList = FXCollections.observableArrayList(employes);
        employeTable.setItems(employeList);
    }

    @FXML
    private void ajouterEmploye() {
        openEmployeForm(null);
    }

    @FXML
    private void modifierEmploye() {
        Employe selectedEmploye = employeTable.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            openEmployeForm(selectedEmploye);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un employé.");
        }
    }

    @FXML
    private void supprimerEmploye() {
        Employe selectedEmploye = employeTable.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            employeService.supprimerEmploye(selectedEmploye.getIdEmp());
            employeList.remove(selectedEmploye);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un employé à supprimer.");
        }
    }

    private void openEmployeForm(Employe employe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/employe_form.fxml"));
            Parent root = loader.load();

            EmployeFormController controller = loader.getController();
            controller.initData(employe, employeService, departementService);

            Stage stage = new Stage();
            stage.setTitle(employe == null ? "Ajouter Employé" : "Modifier Employé");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the table after adding/editing
            loadEmployes();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire !");
            e.printStackTrace();  // Log the actual error
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
