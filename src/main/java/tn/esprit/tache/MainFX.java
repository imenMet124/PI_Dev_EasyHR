package tn.esprit.tache;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // ✅ Apply FlatLaf (for Swing components, if needed)
            UIManager.setLookAndFeel(new FlatLightLaf());

            // ✅ Load JavaFX UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
            Parent root = loader.load();

            // ✅ Apply Custom CSS
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/Theme.css").toExternalForm());

            // ✅ Set Stage Properties
            primaryStage.setTitle("EasyHR - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
