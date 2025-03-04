package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Departement;
import tn.esprit.tache.entities.Employe;
import tn.esprit.tache.services.DepartementService;
import tn.esprit.tache.services.EmployeService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class EmployeFormController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private TextField positionField;
    @FXML private DatePicker dateEmbauchePicker;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<Departement> departementCombo;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private EmployeService employeService;
    private DepartementService departementService;
    private Employe employe;

    public void initData(Employe employe, EmployeService employeService, DepartementService departementService) {
        this.employe = employe;
        this.employeService = employeService;
        this.departementService = departementService;

        // Load Departements into ComboBox
        loadDepartements();

        // Populate Role and Statut Combos
        roleCombo.getItems().addAll("Admin", "Manager", "Employé");
        statutCombo.getItems().addAll("Actif", "Inactif");

        // If editing an existing employee, fill fields
        if (employe != null) {
            nomField.setText(employe.getNomEmp());
            emailField.setText(employe.getEmail());
            roleCombo.setValue(employe.getRole());
            positionField.setText(employe.getPosition());
            statutCombo.setValue(employe.getStatutEmp());
            departementCombo.setValue(employe.getDepartement());

            if (employe.getDateEmbauche() != null) {
                dateEmbauchePicker.setValue(convertDateToLocalDate(employe.getDateEmbauche()));
            }

        }


    }

    @FXML
    private void saveEmploye() {
        if (isInputValid()) {
            if (employe == null) {
                // Create new Employe
                employe = new Employe(
                        nomField.getText(),
                        emailField.getText(),
                        roleCombo.getValue(),
                        positionField.getText(),
                        Date.valueOf(dateEmbauchePicker.getValue()),  // ✅ Convert LocalDate to SQL Date
                        statutCombo.getValue(),
                        departementCombo.getValue()
                );
                employeService.ajouterEmploye(employe);
            } else {
                // Update existing Employe
                employe.setNomEmp(nomField.getText());
                employe.setEmail(emailField.getText());
                employe.setRole(roleCombo.getValue());
                employe.setPosition(positionField.getText());
                employe.setDateEmbauche(Date.valueOf(dateEmbauchePicker.getValue()));  // ✅ Convert LocalDate to SQL Date
                employe.setStatutEmp(statutCombo.getValue());
                employe.setDepartement(departementCombo.getValue());

                employeService.modifierEmploye(employe);
            }
            closeWindow();
        }
    }

    private void loadDepartements() {
        List<Departement> departements = departementService.getAllDepartements();
        departementCombo.getItems().addAll(departements);
    }

    private LocalDate convertDateToLocalDate(java.util.Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();  // ✅ Cas `java.sql.Date`
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();  // ✅ Cas `java.util.Date`
    }


    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Le nom ne peut pas être vide.\n";
        }
        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            errorMessage += "L'email ne peut pas être vide.\n";
        }
        if (roleCombo.getValue() == null) {
            errorMessage += "Veuillez sélectionner un rôle.\n";
        }
        if (positionField.getText() == null || positionField.getText().trim().isEmpty()) {
            errorMessage += "La position ne peut pas être vide.\n";
        }
        if (dateEmbauchePicker.getValue() == null) {
            errorMessage += "Veuillez sélectionner une date d'embauche.\n";
        }
        if (statutCombo.getValue() == null) {
            errorMessage += "Veuillez sélectionner un statut.\n";
        }
        if (departementCombo.getValue() == null) {
            errorMessage += "Veuillez sélectionner un département.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Erreur", errorMessage);
            return false;
        }
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
