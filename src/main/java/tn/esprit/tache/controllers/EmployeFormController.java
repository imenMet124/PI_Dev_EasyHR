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
import java.util.regex.Pattern;

public class EmployeFormController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private TextField positionField;
    @FXML private DatePicker dateEmbauchePicker;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<String> departementCombo; // 🔹 Store department names instead of objects
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private EmployeService employeService;
    private DepartementService departementService;
    private Employe employe;

    public void initData(Employe employe, EmployeService employeService, DepartementService departementService) {
        this.employe = employe;
        this.employeService = employeService;
        this.departementService = departementService;

        // Load departments
        loadDepartements();

        // Populate ComboBoxes for role and status
        roleCombo.getItems().addAll("Admin", "Manager", "Employé");
        statutCombo.getItems().addAll("Actif", "Inactif");

        // If editing an existing employee, prefill fields
        if (employe != null) {
            nomField.setText(employe.getNomEmp());
            emailField.setText(employe.getEmail());
            roleCombo.setValue(employe.getRole());
            positionField.setText(employe.getPosition());
            statutCombo.setValue(employe.getStatutEmp());

            // 🔹 Show department name instead of object
            if (employe.getDepartement() != null) {
                departementCombo.setValue(employe.getDepartement().getNomDep());
            }

            if (employe.getDateEmbauche() != null) {
                dateEmbauchePicker.setValue(convertDateToLocalDate(employe.getDateEmbauche()));
            }
        }
    }

    @FXML
    private void saveEmploye() {
        if (isInputValid()) {
            // 🔹 Retrieve the `Departement` object from the selected department name
            Departement selectedDepartement = departementService.getDepartementByName(departementCombo.getValue());

            if (employe == null) {
                // Creating a new employee
                employe = new Employe(
                        nomField.getText(),
                        emailField.getText(),
                        roleCombo.getValue(),
                        positionField.getText(),
                        Date.valueOf(dateEmbauchePicker.getValue()),
                        statutCombo.getValue(),
                        selectedDepartement // 🔹 Ensure the relation is stored correctly
                );
                employeService.ajouterEmploye(employe);
            } else {
                // Updating an existing employee
                employe.setNomEmp(nomField.getText());
                employe.setEmail(emailField.getText());
                employe.setRole(roleCombo.getValue());
                employe.setPosition(positionField.getText());
                employe.setDateEmbauche(Date.valueOf(dateEmbauchePicker.getValue()));
                employe.setStatutEmp(statutCombo.getValue());
                employe.setDepartement(selectedDepartement); // 🔹 Ensure correct association

                employeService.modifierEmploye(employe);
            }
            closeWindow();
        }
    }

    private void loadDepartements() {
        List<Departement> departements = departementService.getAllDepartements();
        for (Departement dep : departements) {
            departementCombo.getItems().add(dep.getNomDep());  // 🔹 Store only department names
        }
    }

    private LocalDate convertDateToLocalDate(java.util.Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        // Name validation (only letters)
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage.append("Le nom ne peut pas être vide.\n");
        } else if (!Pattern.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+$", nomField.getText())) {
            errorMessage.append("Le nom ne peut contenir que des lettres et espaces.\n");
        }

        // Email validation
        String email = emailField.getText();
        if (email == null || email.trim().isEmpty()) {
            errorMessage.append("L'email ne peut pas être vide.\n");
        } else if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            errorMessage.append("Email invalide !\n");
        } else if (employeService.emailExiste(email) && (employe == null || !email.equals(employe.getEmail()))) {
            errorMessage.append("Cet email est déjà utilisé.\n");
        }

        // Role validation
        if (roleCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un rôle.\n");
        }

        // Position validation
        if (positionField.getText() == null || positionField.getText().trim().isEmpty()) {
            errorMessage.append("La position ne peut pas être vide.\n");
        }

        // Hiring date validation (must not be future)
        if (dateEmbauchePicker.getValue() == null) {
            errorMessage.append("Veuillez sélectionner une date d'embauche.\n");
        } else if (dateEmbauchePicker.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("La date d'embauche ne peut pas être dans le futur.\n");
        }

        // Status validation
        if (statutCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un statut.\n");
        }

        // Department validation
        if (departementCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un département.\n");
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Erreur de saisie", errorMessage.toString());
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
