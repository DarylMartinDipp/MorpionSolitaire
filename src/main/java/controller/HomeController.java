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

/**
 * Controller class for the Home view, responsible for handling user interactions related to launching
 * the Morpion Solitaire game, setting up options (username and mode), and displaying the score board.
 */
public class HomeController {
    public AnchorPane scene1AnchorPane;
    public TextField usernameField;
    public ComboBox<String> gameModeOptions;
    public Button playButton;
    public Button scoreBoardButton;
    private String mode;

    /**
     * Launches the Morpion Solitaire game based on the provided username and game mode.
     * @param event The ActionEvent triggered by the launch action, the playButton.
     * @throws IOException If an I/O error occurs during the loading of the game view.
     */
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

        GameManager gm = new GameManager(mode);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MorpionSolitaire-view.fxml"));
        Parent root = loader.load();
        MorpionSolitaireController mpc = loader.getController();
        mpc.setGameManager(gm);
        mpc.setPlayer(player);
        mpc.setMode(mode);
        //TODO MODIFICATION GUILLAUME , il me fallait une instance de mpc dans ViewMorpionSolitaireh
        ViewMorpionSolitaire.hintController=mpc;
        mpc.start();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets mouse-entered and mouse-exited styles for specific buttons.
     */
    @FXML
    private void initialize() {
        setColorMouseEnteredHome(playButton);
        setColorMouseEnteredHome(scoreBoardButton);
    }

    /**
     * Sets the mouse-entered and mouse-exited styles for a button, changing its background color and its radius.
     * It is specific to the home page.
     * @param button The Button object to which the styles are applied.
     */
    static void setColorMouseEnteredHome(Button button) {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: lightblue; -fx-background-radius: 30 0 0 30;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #6170ba; -fx-background-radius: 30 0 0 30;"));
    }

    /**
     * Loads and displays the score board.
     */
    @FXML
    private void loadScoreBoard() {
        //TODO
        System.out.println("The score board should be displayed.");
    }

    /**
     * Handles the event when the game mode is changed.
     */
    @FXML
    private void gameModeChanged() {
        String selectedMode = gameModeOptions.getSelectionModel().getSelectedItem();
        System.out.println("Selected Mode: " + selectedMode);

        if (("5D".equals(selectedMode) || "5T".equals(selectedMode))) {
            this.mode = selectedMode;
            System.out.println("Updated Mode: " + mode);
        }
    }

    /**
     * Set up the start options. The gameModeOptions ComboBox is updated,as the mode.
     */
    private void setupOptions() {
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5T", "5D");
        gameModeOptions.getSelectionModel().select("5D");
        this.mode = "5D";
    }

    public void start() {
        setupOptions();
    }

    /**
     * Show an alert if the username is not filled.
     */
    private void showAlertUsername() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please enter your username.");
        alert.showAndWait();
    }
}