package tn.esprit.evenement.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.entities.Utilisateur;

import tn.esprit.evenement.services.ServiceEvenement;
import tn.esprit.evenement.services.EmailService;
import tn.esprit.evenement.services.ServiceUtilisateur;
import java.util.List;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class AddEventController {

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteField;
    @FXML private ImageView eventImageView;
    private final EmailService emailService = new EmailService();
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private String imagePath = null;

    //    @FXML
//    private void handleAjouterEvent() {
//        if (!validateFields()) {
//            showAlert("Erreur", "Veuillez remplir tous les champs.");
//            return;
//        }
//
//        Evenement newEvent = new Evenement(
//                titreField.getText(),
//                descriptionField.getText(),
//                Date.valueOf(datePicker.getValue()),
//                Time.valueOf(heureField.getText()),
//                lieuField.getText(),
//                Integer.parseInt(capaciteField.getText()),
//                imagePath
//        );
//
//        try {
//            serviceEvenement.ajouter(newEvent);
//            showAlert("Succès", "Événement ajouté avec succès !");
//            closeWindow();
//        } catch (SQLException e) {
//            showAlert("Erreur", "Problème lors de l'ajout : " + e.getMessage());
//        }
//    }
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
            // Ajouter l'événement à la base de données
            serviceEvenement.ajouter(newEvent);

            // Récupérer les e-mails des utilisateurs
            List<String> emails = serviceUtilisateur.getAllUtilisateurs()
                    .stream()
                    .map(Utilisateur::getEmail)
                    .collect(Collectors.toList());

            // Construire le message de l’événement
            String sujet = "📢 Nouveau événement : " + newEvent.getTitre();
            String contenu = "Un nouvel événement a été ajouté :\n\n" +
                    "📅 Date : " + newEvent.getDate() + "\n" +
                    "⏰ Heure : " + newEvent.getHeure() + "\n" +
                    "📍 Lieu : " + newEvent.getLieu() + "\n\n" +
                    "📝 Description : " + newEvent.getDescription() + "\n\n" +
                    "💼 Merci de consulter la plateforme RH pour plus d’informations.";

            // Envoyer les e-mails en arrière-plan
            new Thread(() -> {
                for (String email : emails) {
                    emailService.envoyerEmail(email, sujet, contenu);
                }
            }).start();

            showAlert("Succès", "Événement ajouté et e-mails envoyés !");
            closeWindow();

        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout : " + e.getMessage());
        } catch (Exception e) {
            showAlert("Avertissement", "L'événement a été ajouté, mais l'envoi des e-mails a échoué.");
            e.printStackTrace();
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

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
