package tn.esprit.evenement.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.services.ServiceEvenement;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ModifyEventsController {

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteField;
    @FXML private TextField participantsField;
    @FXML private ImageView eventImageView;
    @FXML private TextField imagePathField;

    private Evenement eventToModify;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private String newImagePath = null;  // Pour stocker le nouveau chemin de l'image


    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    public void initialize() {
        if (cancelButton != null) {
            cancelButton.setText("❌ Annuler"); // Assurez-vous que le texte est affiché
            FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES);
            closeIcon.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            cancelButton.setGraphic(closeIcon);
            cancelButton.setContentDisplay(ContentDisplay.LEFT); // Afficher icône + texte
        }

        if (saveButton != null) {
            saveButton.setText("💾 Enregistrer"); // Assurez-vous que le texte est affiché
            FontAwesomeIconView saveIcon = new FontAwesomeIconView(FontAwesomeIcon.SAVE);
            saveIcon.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            saveButton.setGraphic(saveIcon);
            saveButton.setContentDisplay(ContentDisplay.LEFT); // Afficher icône + texte
        }

    }

    // ✅ Initialisation des données de l'événement
    public void setEventToModify(Evenement event) {
        this.eventToModify = event;
        titreField.setText(event.getTitre());
        descriptionField.setText(event.getDescription());
        datePicker.setValue(event.getDate().toLocalDate());
        heureField.setText(event.getHeure().toString());
        lieuField.setText(event.getLieu());
        capaciteField.setText(String.valueOf(event.getCapacite()));
        participantsField.setText(String.valueOf(event.getNombreParticipants()));

        // ✅ Vérification et affichage de l'image
        if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
            File file = new File(event.getImagePath());

            if (file.exists()) {
                eventImageView.setImage(new Image(file.toURI().toString()));
                imagePathField.setText(event.getImagePath());
            } else {
                System.out.println("⚠️ L’image spécifiée n’existe pas, utilisation d'une image par défaut.");
              //  eventImageView.setImage(new Image("/images/default.png"));
            }
        }
//        else {
//           eventImageView.setImage(new Image("/images/default.png"));
//       }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                File destDir = new File("C:/events/");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }


                String newFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destFile = new File(destDir, newFileName);

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                eventImageView.setImage(new Image(destFile.toURI().toString()));
                newImagePath = destFile.getAbsolutePath();
                imagePathField.setText(newImagePath);

            } catch (IOException e) {
                System.out.println("⚠️ Erreur lors de l'upload : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifierEvent() {
        if (eventToModify == null) {
            showAlert("Erreur", "Aucun événement sélectionné.");
            return;
        }

        eventToModify.setTitre(titreField.getText());
        eventToModify.setDescription(descriptionField.getText());
        eventToModify.setDate(Date.valueOf(datePicker.getValue()));
        eventToModify.setHeure(Time.valueOf(heureField.getText()));
        eventToModify.setLieu(lieuField.getText());
        eventToModify.setCapacite(Integer.parseInt(capaciteField.getText()));

        if (newImagePath != null) {
            eventToModify.setImagePath(newImagePath);
        }

        try {
            serviceEvenement.modifier(eventToModify);
            showAlert("Succès", "Événement modifié avec succès !");
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de la modification : " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annuler la modification");
        alert.setHeaderText("Voulez-vous vraiment annuler ?");
        alert.setContentText("Les modifications non enregistrées seront perdues.");

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
        titreField.getScene().getWindow().hide(); // Fermer la fenêtre actuelle
    }

}
