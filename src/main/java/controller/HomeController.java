package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Player;

import java.io.IOException;


public class HomeController {
    private String mode;

    @FXML
    private Button playButton;

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
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            showAlertUsername();
            return;
        }

        System.out.println("Hello " + username + ".");
        System.out.println("You will be playing in " + mode + " mode. Good game.");

        Player player = new Player(username);
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
    private void initialize() {
        playButton.setOnMouseEntered(event -> {
            playButton.setStyle("-fx-background-color: lightblue; -fx-background-radius: 30 0 0 30;");
        });

        playButton.setOnMouseExited(event -> playButton.setStyle("-fx-background-color: #6170ba; -fx-background-radius: 30 0 0 30;"));

        scoreBoardButton.setOnMouseEntered(event -> {
            scoreBoardButton.setStyle("-fx-background-color: lightblue; -fx-background-radius: 30 0 0 30;");
        });

        scoreBoardButton.setOnMouseExited(event -> scoreBoardButton.setStyle("-fx-background-color: #6170ba; -fx-background-radius: 30 0 0 30;"));
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

    private void showAlertUsername() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please enter your username.");
        alert.showAndWait();
    }

    public static String setName(TextField usernameField){
        return usernameField.getText().trim();
    }
}