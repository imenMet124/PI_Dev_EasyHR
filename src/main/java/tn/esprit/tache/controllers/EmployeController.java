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
import tn.esprit.tache.services.EmployeService;
import tn.esprit.tache.services.DepartementService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeController {

    @FXML private TableView<Employe> employeTable;
    @FXML private TableColumn<Employe, String> nomCol;
    @FXML private TableColumn<Employe, String> emailCol;
    @FXML private TableColumn<Employe, String> roleCol;
    @FXML private TableColumn<Employe, String> departementCol;
    @FXML private Button addEmployeBtn, editEmployeBtn, deleteEmployeBtn;
    @FXML private TextField searchField;  // ✅ Ajout de la barre de recherche

    private final EmployeService employeService = new EmployeService();
    private final DepartementService departementService = new DepartementService();
    private ObservableList<Employe> employeList;
    private ObservableList<Employe> filteredList; // ✅ Liste filtrée pour la recherche

    @FXML
    public void initialize() {
        // ✅ Bind TableView columns to Employe attributes
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEmp()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

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

        departementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartement().getNomDep()));

        // ✅ Charger les employés
        loadEmployes();

        // ✅ Activer la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterEmployes(newValue));
    }

    private void loadEmployes() {
        List<Employe> employes = employeService.getAllEmployes();
        employeList = FXCollections.observableArrayList(employes);
        filteredList = FXCollections.observableArrayList(employes);
        employeTable.setItems(filteredList);
    }

    private void filterEmployes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredList.setAll(employeList);
            return;
        }

        String lowerCaseKeyword = keyword.toLowerCase();

        List<Employe> filtered = employeList.stream()
                .filter(emp ->
                        emp.getNomEmp().toLowerCase().contains(lowerCaseKeyword) ||
                                emp.getEmail().toLowerCase().contains(lowerCaseKeyword) ||
                                emp.getRole().toLowerCase().contains(lowerCaseKeyword) ||
                                emp.getDepartement().getNomDep().toLowerCase().contains(lowerCaseKeyword)
                )
                .collect(Collectors.toList());

        filteredList.setAll(filtered);
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
            filterEmployes(searchField.getText()); // ✅ Mettre à jour après suppression
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
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
