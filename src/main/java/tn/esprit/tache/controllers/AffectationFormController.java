package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.services.AffectationService;
import tn.esprit.tache.services.EmployeService;
import tn.esprit.tache.services.TacheService;

import java.util.Map;

public class AffectationFormController {

    @FXML private ComboBox<String> employeCombo;
    @FXML private ComboBox<String> tacheCombo;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    private AffectationService affectationService;
    private EmployeService employeService;
    private TacheService tacheService;
    private Map<Integer, String> tacheMap;
    private Map<Integer, String> employeMap;
    public void initData(AffectationService affectationService, EmployeService employeService, TacheService tacheService) {
        this.affectationService = affectationService;
        this.tacheMap = tacheService.getTacheNames();
        this.employeMap = employeService.getEmployeNames();

        employeCombo.getItems().addAll(employeMap.values());
        tacheCombo.getItems().addAll(tacheMap.values());
    }

    @FXML
    private void saveAffectation() {
        if (employeCombo.getValue() == null || tacheCombo.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un employé et une tâche.");
            return;
        }

        // Get selected Name
        String selectedEmploye = employeCombo.getValue();
        String selectedTache = tacheCombo.getValue();

        // Convert Name to ID
        int idEmp = employeMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedEmploye))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);

        int idTache = tacheMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedTache))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);

        // Check if IDs are valid
        if (idEmp == -1 || idTache == -1) {
            showAlert("Erreur", "Problème de correspondance des données.");
            return;
        }

        // Insert Affectation
        affectationService.affecterEmployeTache(idEmp, idTache);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change this to ERROR if needed
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void closeForm() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
}
