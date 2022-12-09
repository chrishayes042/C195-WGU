
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import helper.JDBC;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/login-view.fxml"));
        stage.setScene(new Scene(root));
        ResourceBundle rb = ResourceBundle.getBundle("location", Locale.getDefault());
        stage.setTitle(rb.getString("title"));
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);

    }
}