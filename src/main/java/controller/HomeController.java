package controller;

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Player;

import java.io.IOException;
import java.util.Objects;


public class HomeController {
    private Scene scene;
    private Parent root;

    private String mode;

    @FXML
    private Button PlayButton;

    @FXML
    private ComboBox<String> gameModeOptions;

    @FXML
    private Button historyButton;

    @FXML
    private AnchorPane scene1AnchorPane;

    @FXML
    private TextField usernameField;

    public HomeController() {

    }

    @FXML
    public void OnSwitch2Click(ActionEvent event) throws IOException {
        Player player = new Player(HomeController.setName(usernameField));
        setupOptions();
        GameManager gm = new GameManager(mode);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MorpionSolitaire-view.fxml"));
        Parent root = loader.load();
        MorpionSolitaireController mpc = loader.getController();

        mpc.start();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void displayHistory() {

    }

    @FXML
    private void gameModeChanged(){
        if(Objects.equals(gameModeOptions.getSelectionModel().getSelectedItem(), "5D"))
            mode="5T";
        else if(Objects.equals(gameModeOptions.getSelectionModel().getSelectedItem(), "5T"))
            mode="5D";
    }


    private void setupOptions() {
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");
    }

    public void start() {
        setupOptions();
        reset();
    }

    private void reset() {

    }

    public static String setName(TextField usernameField){
        return usernameField.getText().trim();
    }
}