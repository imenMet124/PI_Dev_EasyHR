package tn.esprit.evenement.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.entities.Utilisateur;
import tn.esprit.evenement.services.EmailService;
import tn.esprit.evenement.services.ServiceEvenement;
import tn.esprit.evenement.services.ServiceUtilisateur;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminEventsController implements Initializable {
    @FXML
    private ListView<Evenement> eventListView;

    @FXML
    private ImageView logoImage;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final EmailService emailService = new EmailService();
    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

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
                Image image = new Image(getClass().getResource("/logo.jpg").toExternalForm());
                logoImage.setImage(image);

                HBox container = new HBox(15);

                // Charger l'image de l'événement
                ImageView eventImageView = new ImageView();
                container.getChildren().add(eventImageView);

                eventImageView.setFitWidth(100); // Largeur de l'image
                eventImageView.setFitHeight(75); // Hauteur de l'image
                eventImageView.setPreserveRatio(true);

                if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {
                    File file = new File(event.getImagePath());
                    if (file.exists()) {
                        eventImageView.setImage(new Image(file.toURI().toString()));
                    }
                }

                container.getStyleClass().add("event-container");
                container.setAlignment(Pos.CENTER_LEFT);

                // Titres et valeurs
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

                // Boutons d'action
                HBox actionBox = new HBox(10);
                actionBox.setAlignment(Pos.CENTER_RIGHT);

                Button modifyButton = createIconButton(FontAwesomeIcon.PENCIL, "modify-button");
                Button deleteButton = createIconButton(FontAwesomeIcon.TRASH, "delete-button");
                Button detailsButton = createIconButton(FontAwesomeIcon.EYE, "details-button");

                modifyButton.setOnAction(e -> handleModifyEvent(event));
                deleteButton.setOnAction(e -> handleDeleteEvent(event));
                detailsButton.setOnAction(e -> handleDetailsEvent(event));

                actionBox.getChildren().addAll(modifyButton, deleteButton);

                // Assemblage final
                container.getChildren().addAll(textContainer, actionBox);
                HBox.setHgrow(textContainer, Priority.ALWAYS);
                setGraphic(container);
            }
        }

        private Button createIconButton(FontAwesomeIcon icon, String styleClass) {
            FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
            iconView.setSize("18");  // Taille idéale pour le bouton
            iconView.getStyleClass().add("event-icon");

            Button button = new Button();
            button.setGraphic(iconView);
            button.getStyleClass().addAll("event-action-button", styleClass);

            // Assurer une vraie forme circulaire
            button.setMinSize(40, 40);
            button.setMaxSize(40, 40);
            button.setShape(new Circle(20)); // IMPORTANT : Forcer la forme du bouton en cercle

            return button;
        }


    }


    private void handleModifyEvent(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifyEventView.fxml"));
            AnchorPane root = loader.load();

            ModifyEventsController controller = loader.getController();
            controller.setEventToModify(event);

            Scene scene = new Scene(root, 800, 600);
            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier l'événement");
            modifyStage.setScene(scene);
            modifyStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteEvent(Evenement event) {
        try {

            serviceEvenement.supprimer(event.getId());
            List<String> emails = serviceUtilisateur.getAllUtilisateurs()
                    .stream()
                    .filter(u -> u.getIyedRoleUser() == UserRole.RESPONSABLE_RH) // Filter by RESPONSABLE_RH
                    .map(User::getIyedEmailUser) // Map to email
                    .collect(Collectors.toList());

            if (!emails.isEmpty()) {

                String sujet = "⚠️ Suppression d’un événement : " + event.getTitre();
                String contenu = "L'événement suivant a été supprimé :\n\n" +
                        "📅 Date : " + event.getDate() + "\n" +
                        "⏰ Heure : " + event.getHeure() + "\n" +
                        "📍 Lieu : " + event.getLieu() + "\n\n" +
                        "📝 Description : " + event.getDescription() + "\n\n" +
                        "❗ Cette suppression a été effectuée par l'administration RH.\n\n" +
                        "💼 Pour toute question, veuillez contacter l’équipe RH.";

                new Thread(() -> {
                    for (String email : emails) {
                        emailService.envoyerEmail(email, sujet, contenu);
                    }
                }).start();
            }

            loadEvents();

            showAlert("Succès", "Événement supprimé et notification envoyée aux Admin RH !");

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Avertissement", "L'événement a été supprimé, mais l'envoi des e-mails a échoué.");
            e.printStackTrace();
        }
    }


    private void handleDetailsEvent(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEventView.fxml"));
            AnchorPane root = loader.load();

            // Récupération du contrôleur et passage de l'événement
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
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleRefresh() {
        System.out.println("Actualisation de la liste des événements...");
        loadEvents();
    }

    @FXML
    private void handleAddEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEventView.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            Stage addStage = new Stage();
            addStage.setTitle("Ajouter un événement");
            addStage.setScene(scene);
            addStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
