package tn.esprit.formations.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        //   FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminView.fxml"));
   //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/UserView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User Panel");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}