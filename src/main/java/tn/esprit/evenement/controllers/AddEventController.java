package tn.esprit.evenement.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.entities.Utilisateur;
import tn.esprit.evenement.services.NominatimService;
import tn.esprit.evenement.services.ServiceEvenement;
import tn.esprit.evenement.services.EmailService;
import tn.esprit.evenement.services.ServiceUtilisateur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

public class AddEventController {

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteField;
    @FXML private ImageView eventImageView;
    @FXML private ListView<String> suggestionsListView;

    private final EmailService emailService = new EmailService();
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private String imagePath = null;

    @FXML
    public void initialize() {

        // Gérer l'autocomplétion du champ "Lieu"
        lieuField.setOnKeyReleased(this::handleLocationTyping);

        // Sélectionner un lieu dans la liste
        suggestionsListView.setOnMouseClicked(event -> {
            String selectedPlace = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedPlace != null) {
                lieuField.setText(selectedPlace);
                suggestionsListView.setVisible(false);
            }
        });


    }

    private void handleLocationTyping(KeyEvent event) {
        String input = lieuField.getText().trim();

        List<String> suggestions = NominatimService.getPlaceSuggestions(input);
        System.out.println("📌 Suggestions pour '" + input + "' : " + suggestions);

        if (!suggestions.isEmpty()) {
            suggestionsListView.getItems().setAll(suggestions);
            suggestionsListView.setVisible(true);

            // Ajuster la hauteur en fonction du nombre de suggestions
            suggestionsListView.setPrefHeight(Math.min(suggestions.size() * 30, 200));

            Platform.runLater(() -> {
                // Obtenir les coordonnées du champ "lieuField" dans la scène
                double fieldX = lieuField.localToScene(0, 0).getX();
                double fieldY = lieuField.localToScene(0, 0).getY() + lieuField.getHeight();

                // Récupérer les coordonnées de la fenêtre principale
                double windowX = lieuField.getScene().getWindow().getX();
                double windowY = lieuField.getScene().getWindow().getY();

                // Placer la liste **juste en dessous** du champ "Lieu"
                suggestionsListView.setLayoutX(windowX + fieldX);
                suggestionsListView.setLayoutY(windowY + fieldY);
                suggestionsListView.toFront(); // Mettre la liste au premier plan
            });

        } else {
            suggestionsListView.setVisible(false);
        }
    }







    @FXML
    private void handleAjouterEvent() {
        if (!validateFields()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Evenement newEvent = new Evenement(
                titreField.getText(),
                descriptionField.getText(),
                Date.valueOf(datePicker.getValue()),
                Time.valueOf(heureField.getText()),
                lieuField.getText(),
                Integer.parseInt(capaciteField.getText()),
                imagePath
        );

        try {
            serviceEvenement.ajouter(newEvent);

            // Envoyer les emails uniquement aux Admin RH
            List<String> emails = serviceUtilisateur.getAllUtilisateurs()
                    .stream()
                    .filter(u -> u.getRole() == Utilisateur.Role.AdminRH)
                    .map(Utilisateur::getEmail)
                    .collect(Collectors.toList());

            String sujet = "📢 Nouveau événement : " + newEvent.getTitre();
            String contenu = "Un nouvel événement a été ajouté :\n\n" +
                    "📅 Date : " + newEvent.getDate() + "\n" +
                    "⏰ Heure : " + newEvent.getHeure() + "\n" +
                    "📍 Lieu : " + newEvent.getLieu() + "\n\n" +
                    "📝 Description : " + newEvent.getDescription() + "\n\n" +
                    "💼 Merci de consulter la plateforme RH pour plus d’informations.";

            new Thread(() -> emails.forEach(email -> emailService.envoyerEmail(email, sujet, contenu))).start();

            showAlert("Succès", "Événement ajouté et e-mails envoyés aux Admin RH !");
            closeWindow();

        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout : " + e.getMessage());
        }
    }
    private boolean validateFields() {
        return !titreField.getText().isEmpty() &&
                !descriptionField.getText().isEmpty() &&
                datePicker.getValue() != null &&
                !heureField.getText().isEmpty() &&
                !lieuField.getText().isEmpty() &&
                !capaciteField.getText().isEmpty();
    }
    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                File destDir = new File("C:/images/events/");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                String newFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destFile = new File(destDir, newFileName);

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                eventImageView.setImage(new Image(destFile.toURI().toString()));
                imagePath = destFile.getAbsolutePath();

            } catch (IOException e) {
                showAlert("Erreur", "Erreur lors de l'upload : " + e.getMessage());
            }
        }
    }
    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annuler l'ajout");
        alert.setHeaderText("Voulez-vous vraiment annuler ?");
        alert.setContentText("Les informations non enregistrées seront perdues.");

        ButtonType boutonOui = new ButtonType("Oui");
        ButtonType boutonNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(boutonOui, boutonNon);

        alert.showAndWait().ifPresent(response -> {
            if (response == boutonOui) {
                closeWindow();
            }
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
