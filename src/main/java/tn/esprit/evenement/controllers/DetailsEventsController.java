package tn.esprit.evenement.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.services.TranslationService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DetailsEventsController {

    @FXML private ImageView eventImageView;
    @FXML private Label titreLabel;
    @FXML private Label dateLabel;
    @FXML private Label heureLabel;
    @FXML private Label lieuLabel;
    @FXML private Label capaciteLabel;
    @FXML private Label participantsLabel;
    @FXML private TextFlow descriptionContainer;
    @FXML private Button closeButton;
    @FXML private Button translateButton;
    @FXML private ChoiceBox<String> languageChoiceBox;

    private Evenement evenement;
    private String originalDescription;
    private String translatedDescription;
    private String selectedLanguageCode = "en"; // Par défaut : Anglais

    private static final Map<String, String> LANGUAGES = new HashMap<>();

    static {
        LANGUAGES.put("Anglais", "en");
        LANGUAGES.put("Espagnol", "es");
        LANGUAGES.put("Allemand", "de");
    }

    public void setEventDetails(Evenement event) {
        this.evenement = event;

        titreLabel.setText(event.getTitre());
        dateLabel.setText(event.getDate().toString());
        heureLabel.setText(event.getHeure().toString());
        lieuLabel.setText(event.getLieu());
        capaciteLabel.setText(String.valueOf(event.getCapacite()));
        participantsLabel.setText(String.valueOf(event.getNombreParticipants()));

        originalDescription = event.getDescription();
        Text descriptionText = new Text(originalDescription);
        descriptionContainer.getChildren().clear();
        descriptionContainer.getChildren().add(descriptionText);

        // Configuration du menu de sélection de langue
        languageChoiceBox.setValue("Anglais");
        languageChoiceBox.setOnAction(e -> selectedLanguageCode = LANGUAGES.get(languageChoiceBox.getValue()));

        translatedDescription = null; // Réinitialiser la traduction
        if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
            File file = new File(event.getImagePath());
            if (file.exists()) {
                eventImageView.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    @FXML
    private void handleTranslate() {
        String currentText = ((Text) descriptionContainer.getChildren().get(0)).getText();

        if (!currentText.equals(originalDescription)) {
            // Revenir à la version originale
            descriptionContainer.getChildren().clear();
            descriptionContainer.getChildren().add(new Text(originalDescription));
            translateButton.setText("🔄 Traduire");
            translatedDescription = null;
        } else {
            // Éviter une requête inutile si la traduction a déjà été effectuée
            if (translatedDescription == null) {
                translatedDescription = TranslationService.translate(originalDescription, "fr", selectedLanguageCode);
            }

            descriptionContainer.getChildren().clear();
            descriptionContainer.getChildren().add(new Text(translatedDescription));
            translateButton.setText("🔄 Revenir à l'original");
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
