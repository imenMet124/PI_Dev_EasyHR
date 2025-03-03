package tn.esprit.Offres.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PageOffre extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage secondaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/offre ui.fxml"));
        Parent root = loader.load();
        OffresController controller = loader.getController();
        Scene scene = new Scene(root);
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }
}