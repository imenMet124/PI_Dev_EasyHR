package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.services.*;

import tn.esprit.tache.services.AffectationService;
import tn.esprit.tache.services.ServiceUsers;
import tn.esprit.tache.services.TacheService;

import java.util.Map;

public class AffectationFormController {

    @FXML private ComboBox<String> employeCombo;
    @FXML private ComboBox<String> tacheCombo;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    private AffectationService affectationService;

    private Map<Integer, String> tacheMap;
    private Map<Integer, String> employeMap;
    private ServiceUsers employeService;
    private TacheService tacheService;




    public void initData(AffectationService affectationService, ServiceUsers employeService, TacheService tacheService, ProjetService projetService) {
        this.affectationService = affectationService;
        this.tacheMap = tacheService.getTacheNames();
        this.employeMap = employeService.getUsersNames();

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

        showAlert("Succès", "Affectation enregistrée avec succès.");

        // Close the form after saving
        closeForm();
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