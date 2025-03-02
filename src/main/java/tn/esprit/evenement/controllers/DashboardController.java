package tn.esprit.evenement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.services.ServiceEvenement;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private AnchorPane dashboardView;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Label weekLabel;

    private ServiceEvenement evenementService = new ServiceEvenement();
    private LocalDate currentWeekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        weekLabel.setText("Semaine du " + currentWeekStart + " au " + currentWeekStart.plusDays(6));

        // Ajout des jours en entête
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(currentWeekStart.plusDays(i).getDayOfWeek().toString());
            calendarGrid.add(dayLabel, i + 1, 0);
        }

        // Ajout des heures sur la première colonne
        for (int hour = 8; hour <= 18; hour++) {
            Label hourLabel = new Label(hour + ":00");
            calendarGrid.add(hourLabel, 0, hour - 7);
        }

        // Récupérer et afficher les événements
        try {
            List<Evenement> evenements = evenementService.getEventsByWeek(currentWeekStart);
            System.out.println("Événements récupérés pour la semaine " + currentWeekStart + " : " + evenements.size());

            for (Evenement event : evenements) {
                System.out.println("Événement trouvé : " + event.getTitre() + " - " + event.getDate() + " " + event.getHeure());
                addEventToCalendar(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addEventToCalendar(Evenement event) {
        int dayColumn = event.getDate().toLocalDate().getDayOfWeek().getValue() - 1; // ✅ Correction ici
        int startRow = event.getHeure().toLocalTime().getHour() - 7; // ✅ Évite les heures négatives
        int duration = 1; // Peut être amélioré si on récupère la durée réelle

        Label eventLabel = new Label(event.getTitre());
        eventLabel.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");

        GridPane.setRowSpan(eventLabel, duration);

        // ✅ Vérifier si l'index est valide avant d'ajouter
        if (dayColumn >= 0 && dayColumn < 7 && startRow >= 0 && startRow < 12) {
            calendarGrid.add(eventLabel, dayColumn + 1, startRow + 1); // ✅ Décalage pour aligner avec les en-têtes
        } else {
            System.out.println("Erreur : Position invalide pour l'événement " + event.getTitre());
        }
    }


    @FXML
    private void previousWeek() {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        updateCalendar();
    }

    @FXML
    private void nextWeek() {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        updateCalendar();
    }
}
