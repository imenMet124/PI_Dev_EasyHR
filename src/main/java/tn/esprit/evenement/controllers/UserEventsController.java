package tn.esprit.evenement.controllers;

import javafx.fxml.FXML;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.entities.Participation;
import tn.esprit.evenement.entities.Utilisateur;
import tn.esprit.evenement.services.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserEventsController implements Initializable {
    @FXML
    private ListView<Evenement> eventListView;

    @FXML
    private ImageView logoImage;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();
    private final EmailService emailService = new EmailService();

    // 🔹 Définition de l'utilisateur courant (à modifier selon ton authentification)
    private Utilisateur currentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (eventListView == null) {
            System.out.println("ERREUR : eventListView est NULL !");
        } else {
            System.out.println("SUCCESS : eventListView est bien initialisée !");
        }
        loadEvents();
        eventListView.setCellFactory(param -> new EventListCell());
    }

    void loadEvents() {
        try {
            ObservableList<Evenement> events = FXCollections.observableArrayList(serviceEvenement.afficher());
            System.out.println("Nombre d'événements chargés : " + events.size());
            eventListView.setItems(events);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des événements: " + e.getMessage());
        }
    }

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
    }

    public void goToIndex(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/IndexView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListCell extends ListCell<Evenement> {
        @Override
        protected void updateItem(Evenement event, boolean empty) {
            super.updateItem(event, empty);
            if (empty || event == null) {
                setGraphic(null);
            } else {
                HBox container = new HBox(15);
                container.getStyleClass().add("event-container");
                container.setAlignment(Pos.CENTER_LEFT);

                // 📌 Charger l'image de l'événement
                ImageView eventImageView = new ImageView();
                eventImageView.setFitWidth(100);
                eventImageView.setFitHeight(75);
                eventImageView.setPreserveRatio(true);

                if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
                    File file = new File(event.getImagePath());
                    if (file.exists()) {
                        eventImageView.setImage(new Image(file.toURI().toString()));
                    }
                }

                // 📌 Titres et valeurs
                VBox textContainer = new VBox(5);
                Label title = new Label(event.getTitre());
                title.getStyleClass().add("event-title");

                Label date = new Label("📅 " + event.getDate().toString());
                date.getStyleClass().add("event-label");

                Label heure = new Label("⏰ " + event.getHeure().toString());
                heure.getStyleClass().add("event-label");

                Label capacity = new Label("👥 Capacité: " + event.getCapacite());
                capacity.getStyleClass().add("event-label");

                Label participants = new Label("🎟 Participants: " + event.getNombreParticipants());
                participants.getStyleClass().add("event-label");

                textContainer.getChildren().addAll(title, date, heure, capacity, participants);


                // 📌 Boutons d'action
                HBox actionBox = new HBox(10);
                actionBox.setAlignment(Pos.CENTER_RIGHT);

                // 🔹 Bouton "S'inscrire" avec icône
                Button participateButton = new Button();
                FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
                iconView.setSize("18");
                iconView.getStyleClass().add("participate-icon");

                participateButton.setGraphic(iconView);
                participateButton.getStyleClass().add("participate-button");
                participateButton.setOnAction(e -> handleInscription(event));
                // Assurer une vraie forme circulaire
                participateButton.setMinSize(40, 40);
                participateButton.setMaxSize(40, 40);
                participateButton.setShape(new Circle(20));

                // 🔹 Bouton "Détails"
                Button detailsButton = createIconButton(FontAwesomeIcon.EYE, "details-button");
                detailsButton.setOnAction(e -> handleDetailsEvent(event));

                actionBox.getChildren().addAll(participateButton, detailsButton);

                // 📌 Assemblage final
                container.getChildren().addAll(eventImageView, textContainer, actionBox);
                HBox.setHgrow(textContainer, Priority.ALWAYS);
                setGraphic(container);
            }
        }

        private Button createIconButton(FontAwesomeIcon icon, String styleClass) {
            FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
            iconView.setSize("18");
            iconView.getStyleClass().add("event-icon");

            Button button = new Button();
            button.setGraphic(iconView);
            button.getStyleClass().addAll("event-action-button", styleClass);

            button.setMinSize(40, 40);
            button.setMaxSize(40, 40);
            button.setShape(new Circle(20));

            return button;
        }
    }
    @FXML
    private void handleRefresh() {
        System.out.println("Actualisation de la liste des événements...");
        loadEvents();
    }

    private void handleDetailsEvent(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEventView.fxml"));
            AnchorPane root = loader.load();

            DetailsEventsController controller = loader.getController();
            controller.setEventDetails(event);

            Scene scene = new Scene(root, 900, 600);
            Stage detailsStage = new Stage();
            detailsStage.setTitle("Détails de l'événement");
            detailsStage.setScene(scene);
            detailsStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInscription(Evenement selectedEvent) {
        if (selectedEvent != null) {
            LocalDate currentDate = LocalDate.now();

            if (selectedEvent.getNombreParticipants() >= selectedEvent.getCapacite()) {
                showAlert("Erreur", "Cet événement est complet.");
                return;
            }

            boolean alreadyRegistered = serviceParticipation.getParticipationsParUtilisateur(currentUser.getId())
                    .stream().anyMatch(p -> p.getEvenement().getId() == selectedEvent.getId());

            if (alreadyRegistered) {
                showAlert("Erreur", "Vous êtes déjà inscrit.");
                return;
            }

            Participation newParticipation = new Participation(selectedEvent, currentUser, currentDate, "En attente");
            serviceParticipation.ajouterParticipation(newParticipation);

            showAlert("Succès", "Inscription confirmée !");
            loadEvents();
        } else {
            showAlert("Erreur", "Sélectionnez un événement.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
