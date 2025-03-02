package tn.esprit.Offres.test;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.Offres.controllers.HomeController;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        Scene scene = new Scene(loader.load());
        HomeController controller = loader.getController();
        stage.setTitle("Test Home Page");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
