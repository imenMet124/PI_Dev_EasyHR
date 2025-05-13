package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.services.ProjetService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

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

        // Remplir le ComboBox du statut
        statutCombo.getItems().addAll("En cours", "Terminé", "Annulé");

        // Si modification d'un projet, pré-remplir les champs
        if (projet != null) {
            nomField.setText(projet.getNomProjet());
            descField.setText(projet.getDescProjet());
            statutCombo.setValue(projet.getStatutProjet());

            dateDebutPicker.setValue(convertDateToLocalDate(projet.getDateDebutProjet()));
            dateFinPicker.setValue(convertDateToLocalDate(projet.getDateFinProjet()));
        } else {
            statutCombo.setValue("En cours");
            statutCombo.setDisable(true);
        }
    }

    @FXML
    private void saveProjet() {
        if (isInputValid()) {
            if (projet == null) {
                // Création d'un nouveau projet
                projet = new Projet(
                        nomField.getText(),
                        descField.getText(),
                        statutCombo.getValue(),
                        convertLocalDateToDate(dateDebutPicker.getValue()),
                        convertLocalDateToDate(dateFinPicker.getValue())
                );
                projetService.ajouterProjet(projet);
            } else {
                // Mise à jour d'un projet existant
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
        StringBuilder errorMessage = new StringBuilder();

        // Vérification du nom (lettres, chiffres et espaces uniquement)
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage.append("Le nom du projet ne peut pas être vide.\n");
        } else if (!Pattern.matches("^[A-Za-z0-9À-ÖØ-öø-ÿ\\s-]+$", nomField.getText())) {
            errorMessage.append("Le nom du projet ne peut contenir que des lettres, chiffres et espaces.\n");
        }

        // Vérification de la description (au moins 10 caractères)
        if (descField.getText() == null || descField.getText().trim().isEmpty()) {
            errorMessage.append("La description du projet ne peut pas être vide.\n");
        } else if (descField.getText().trim().length() < 10) {
            errorMessage.append("La description doit contenir au moins 10 caractères.\n");
        }

        // Vérification du statut
        if (statutCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un statut.\n");
        }

        // Vérification de la date de début (obligatoire et ne peut pas être future)
        if (dateDebutPicker.getValue() == null) {
            errorMessage.append("Veuillez sélectionner une date de début.\n");
        } else if (dateDebutPicker.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("La date de début ne peut pas être dans le futur.\n");
        }

        // Vérification de la date de fin (obligatoire et après la date de début)
        if (dateFinPicker.getValue() == null) {
            errorMessage.append("Veuillez sélectionner une date de fin.\n");
        } else if (dateDebutPicker.getValue() != null && dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            errorMessage.append("La date de fin doit être après la date de début.\n");
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

    // ✅ Convertir java.util.Date en LocalDate
    private LocalDate convertDateToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ✅ Convertir LocalDate en java.util.Date
    private Date convertLocalDateToDate(LocalDate localDate) {
        return (localDate != null) ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }
}
