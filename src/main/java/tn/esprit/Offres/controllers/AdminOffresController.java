package tn.esprit.Offres.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.services.ServiceOffres;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminOffresController {

    @FXML
    private VBox offresContainer;

    private ServiceOffres serviceOffre;

    public AdminOffresController() {
        this.serviceOffre = new ServiceOffres();
    }

    @FXML
    public void initialize() {
        try {
            afficherOffres();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherOffres() throws SQLException {
        List<Offre> offres = serviceOffre.afficher();

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

            textContainer.getChildren().addAll(titreLabel, descriptionLabel, dateLabel);

            Button modifierButton = new Button("Modifier");
            modifierButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-background-radius: 5;");
            modifierButton.setOnAction(event -> modifierOffre(offre));

            Button supprimerButton = new Button("Supprimer");
            supprimerButton.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-background-radius: 5;");
            supprimerButton.setOnAction(event -> supprimerOffre(offre));

            offreCard.getChildren().addAll(imageView, textContainer, modifierButton, supprimerButton);
            offresContainer.getChildren().add(offreCard);
        }
    }

    private void modifierOffre(Offre offre) {
        try {
            serviceOffre.modifier(offre);
            offresContainer.getChildren().clear();
            afficherOffres();
            System.out.println("L'offre a été modifiée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void supprimerOffre(Offre offre) {
        try {
            serviceOffre.supprimer(offre.getIdOffre());
            offresContainer.getChildren().clear();
            afficherOffres();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void afficherCandidatures() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidatures.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Candidatures");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
