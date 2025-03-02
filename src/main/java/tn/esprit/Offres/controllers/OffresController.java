package tn.esprit.Offres.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Offres.entities.Candidature;
import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.entities.User;
import tn.esprit.Offres.services.ServiceCandidat;
import tn.esprit.Offres.services.ServiceCandidature;
import tn.esprit.Offres.services.ServiceOffres;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OffresController {

    @FXML
    private VBox offresContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<Offre.StatutOffre> statutComboBox;

    @FXML
    private Button searchButton;

    private final ServiceOffres serviceOffre;

    public OffresController() {
        this.serviceOffre = new ServiceOffres();
    }

    @FXML
    public void initialize() {
        // ✅ Ajouter les valeurs ENUM au ComboBox (Statuts)
        statutComboBox.getItems().setAll(Offre.StatutOffre.values());

        // ✅ Charger toutes les offres au démarrage
        try {
            afficherOffres(serviceOffre.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // ✅ Connecter le bouton de recherche à la méthode de filtrage
        searchButton.setOnAction(event -> rechercherOffres());
    }

    private void rechercherOffres() {
        try {
            String rechercheTexte = searchField.getText().trim().toLowerCase();
            Offre.StatutOffre statutSelectionne = statutComboBox.getValue();

            // ✅ Récupérer toutes les offres depuis la BD
            List<Offre> toutesLesOffres = serviceOffre.afficher();

            // ✅ Appliquer les filtres de recherche
            List<Offre> offresFiltrées = toutesLesOffres.stream()
                    .filter(offre -> offre.getTitrePoste().toLowerCase().contains(rechercheTexte))
                    .filter(offre -> statutSelectionne == null || offre.getStatuOffre() == statutSelectionne) // Filtrer par statut
                    .collect(Collectors.toList());

            afficherOffres(offresFiltrées);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la recherche.");
        }
    }

    private void afficherOffres(List<Offre> offres) {
        offresContainer.getChildren().clear(); // Nettoyer l'affichage

        for (Offre offre : offres) {
            HBox offreCard = new HBox(10);
            offreCard.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-box-shadow: 2px 2px 10px rgba(0,0,0,0.1);");

            ImageView imageView = new ImageView();
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);

            VBox textContainer = new VBox(5);
            Label titreLabel = new Label(offre.getTitrePoste());
            titreLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            Label descriptionLabel = new Label(offre.getDescription());
            descriptionLabel.setStyle("-fx-text-fill: #666;");

            Label dateLabel = new Label("Date de publication: " + offre.getDatePublication());
            dateLabel.setStyle("-fx-text-fill: #999;");

            // ✅ Ajout du Label pour afficher le statut
            Label statutLabel = new Label("Statut: " + offre.getStatuOffre());
            statutLabel.setStyle(getStatutStyle(offre.getStatuOffre()));

            textContainer.getChildren().addAll(titreLabel, descriptionLabel, dateLabel, statutLabel);

            Button postulerButton = new Button("Postuler");
            postulerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
            postulerButton.setOnAction(event -> postulerOffre(offre));

            offreCard.getChildren().addAll(imageView, textContainer, postulerButton);
            offresContainer.getChildren().add(offreCard);
        }
    }

    // ✅ Appliquer un style de couleur différent selon le statut
    private String getStatutStyle(Offre.StatutOffre statut) {
        switch (statut) {
            case EN_COURS:
                return "-fx-text-fill: #E67E22; -fx-font-weight: bold;"; // Orange pour "En Cours"
            case OUVERT:
                return "-fx-text-fill: #2ECC71; -fx-font-weight: bold;"; // Vert pour "Ouvert"
            case FERME:
                return "-fx-text-fill: #E74C3C; -fx-font-weight: bold;"; // Rouge pour "Fermé"
            default:
                return "-fx-text-fill: #000;"; // Noir par défaut
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void postulerOffre(Offre offre) {
        try {
            // Ensure the offer has a valid ID
            if (offre.getIdOffre() == 0) {
                System.out.println("❌ Erreur : L'offre avec ID 0 n'est pas valide !");
                return;
            }

            // Charger la page de confirmation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidat.fxml"));
            Parent root = loader.load();
            CandidatController candidatController = loader.getController();

            User userConnecte = new User(
                    1,
                    "Dupont",
                    "dupont@example.com",
                    "12345678",
                    "Employé",
                    "Développeur",
                    3000.0,
                    new java.util.Date(),
                    "Actif",
                    "Informatique"
            );

            // Remplir les informations avec l'utilisateur connecté
            candidatController.remplirInformationsCandidat(userConnecte);

            // Afficher la fenêtre de confirmation des informations
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Ajouter le candidat et récupérer son ID
            int idCandidat = candidatController.ajouterCandidatEtRetournerId();
            if (idCandidat == -1) {
                System.out.println("❌ Erreur lors de la création du candidat.");
                return;
            }

            // Vérification après ajout du candidat
            System.out.println("✅ Candidat ajouté avec ID: " + idCandidat);

            // Récupération du candidat depuis la base de données
            ServiceCandidat serviceCandidat = new ServiceCandidat();
            Candidat candidatAjoute = serviceCandidat.getCandidatById(idCandidat);
            if (candidatAjoute == null) {
                System.out.println("❌ Erreur : Impossible de récupérer le candidat après ajout.");
                return;
            }
            System.out.println("📌 Vérification des données avant ajout de la candidature :");
            System.out.println("📌 ID Candidat: " + candidatAjoute.getIdCandidat());
            System.out.println("📌 Nom: " + candidatAjoute.getNom());
            System.out.println("📌 Email: " + candidatAjoute.getEmail());
            System.out.println("📌 ID Offre: " + offre.getIdOffre());
            System.out.println("📌 Titre Offre: " + offre.getTitrePoste());

            // Vérification avant insertion
            ServiceOffres serviceOffres = new ServiceOffres();

            // Création et enregistrement de la candidature
            Candidature candidature = new Candidature();
            candidature.setCandidat(candidatAjoute);
            candidature.setOffre(offre); // Use the verified offer
            candidature.setDateCandidature(LocalDate.now());
            candidature.setStatutCandidature(Candidature.StatutCandidature.EN_ATTENTE);

            // Ajouter la candidature
            ServiceCandidature serviceCandidature = new ServiceCandidature();
            int idCandidature = serviceCandidature.ajouterCandidature(candidature);

            if (idCandidature != -1) {
                System.out.println("✅ Candidature ajoutée avec succès !");
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre candidature a été soumise avec succès !");
            } else {
                System.out.println("❌ Erreur lors de l'ajout de la candidature.");
            }

        } catch (IOException e) {
            System.out.println("❌ Erreur lors du processus de candidature : " + e.getMessage());
        }
    }




}
