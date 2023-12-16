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
    private Button scoreBoardButton;

    @FXML
    private AnchorPane scene1AnchorPane;

    @FXML
    private TextField usernameField;

    public HomeController() {

    }

    @FXML
    public void launchTheGame(ActionEvent event) throws IOException {
        System.out.println("Hello " + usernameField.getText() + ".");
        System.out.println("You will be playing in " + mode + " mode. Good game.");

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
    private void loadScoreBoard() {
        //TODO
        System.out.println("The score board should be displayed.");
    }

    @FXML
    private void gameModeChanged(){
        String selectedMode = gameModeOptions.getSelectionModel().getSelectedItem();

        if ("5D".equals(selectedMode) || "5T".equals(selectedMode))
            this.mode = selectedMode;
    }

    private void setupOptions() {
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");
        this.mode = "5D";
    }

    public void start() {
        setupOptions();
    }

    public static String setName(TextField usernameField){
        return usernameField.getText().trim();
    }
}