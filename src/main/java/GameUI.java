import controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();
        HomeController hc= loader.getController();
        Scene scene = new Scene(root);
        hc.start();

        stage.setTitle("Morpion Solitaire");
        stage.setScene(scene);
        stage.show();
    }
}