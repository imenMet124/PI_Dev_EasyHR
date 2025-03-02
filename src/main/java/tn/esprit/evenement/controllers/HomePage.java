package tn.esprit.evenement.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage extends Application {

    @FXML
    private ImageView logoImage;

    @FXML
    private StackPane contentArea; // Conteneur où les vues seront chargées

    private AnchorPane dashboardView;
    private AnchorPane eventsView;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/IndexView.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setTitle("Application RH");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de IndexView.fxml");
        }
    }

    @FXML
    public void initialize() {
        try {
            if (logoImage != null) {
                Image image = new Image(getClass().getResource("/logo.jpg").toExternalForm());
                logoImage.setImage(image);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du logo : " + e.getMessage());
        }

        showDashboard();
    }

    @FXML
    public void showDashboard() {
        try {
            if (dashboardView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardView.fxml"));
                dashboardView = loader.load(); // ✅ Ne pas caster en VBox

            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du Dashboard.");
        }
    }

    @FXML
    public void handleListEvent() {
        try {
            if (eventsView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminEventsView.fxml"));
                eventsView = loader.load();
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(eventsView);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des événements.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
