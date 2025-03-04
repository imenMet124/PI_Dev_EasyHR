package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.services.ProjetService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ProjetFormController {

    @FXML private TextField nomField;
    @FXML private TextArea descField;
    @FXML private ComboBox<String> statutCombo;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private ProjetService projetService;
    private Projet projet;

    public void initData(Projet projet, ProjetService projetService) {
        this.projet = projet;
        this.projetService = projetService;

        // Populate the status combo box
        statutCombo.getItems().addAll("En cours", "Terminé", "Annulé");

        // If editing an existing project, fill the fields
        if (projet != null) {
            nomField.setText(projet.getNomProjet());
            descField.setText(projet.getDescProjet());
            statutCombo.setValue(projet.getStatutProjet());

            dateDebutPicker.setValue(convertDateToLocalDate(projet.getDateDebutProjet()));
            dateFinPicker.setValue(convertDateToLocalDate(projet.getDateFinProjet()));
        }else {
            statutCombo.setValue("En cours");
            statutCombo.setDisable(true);
        }
    }

    @FXML
    private void saveProjet() {
        if (isInputValid()) {
            if (projet == null) {
                // Create a new project
                projet = new Projet(
                        nomField.getText(),
                        descField.getText(),
                        statutCombo.getValue(),
                        convertLocalDateToDate(dateDebutPicker.getValue()),
                        convertLocalDateToDate(dateFinPicker.getValue())
                );
                projetService.ajouterProjet(projet);
            } else {
                // Update existing project
                projet.setNomProjet(nomField.getText());
                projet.setDescProjet(descField.getText());
                projet.setStatutProjet(statutCombo.getValue());
                projet.setDateDebutProjet(convertLocalDateToDate(dateDebutPicker.getValue()));
                projet.setDateFinProjet(convertLocalDateToDate(dateFinPicker.getValue()));

                projetService.modifierProjet(projet);
            }
            closeWindow();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Le nom du projet ne peut pas être vide.\n";
        }
        if (descField.getText() == null || descField.getText().trim().isEmpty()) {
            errorMessage += "La description du projet ne peut pas être vide.\n";
        }
        if (statutCombo.getValue() == null) {
            errorMessage += "Veuillez sélectionner un statut.\n";
        }
        if (dateDebutPicker.getValue() == null) {
            errorMessage += "Veuillez sélectionner une date de début.\n";
        }
        if (dateFinPicker.getValue() == null) {
            errorMessage += "Veuillez sélectionner une date de fin.\n";
        }
        if (dateDebutPicker.getValue() != null && dateFinPicker.getValue() != null &&
                dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            errorMessage += "La date de fin doit être après la date de début.\n";
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

    // ✅ Convert java.util.Date to LocalDate
    private LocalDate convertDateToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    // ✅ Convert LocalDate to java.util.Date
    private Date convertLocalDateToDate(LocalDate localDate) {
        return (localDate != null) ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }
}
