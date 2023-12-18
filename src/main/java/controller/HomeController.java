package controller;

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Player;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
    public static String selectedMode;

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

        Player player = new Player(username);

        GameManager gm = new GameManager(mode);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MorpionSolitaire-view.fxml"));
        Parent root = loader.load();
        MorpionSolitaireController mpc = loader.getController();
        mpc.setGameManager(gm);
        mpc.setPlayer(player);
        mpc.setMode(mode);
        ViewMorpionSolitaire.hintController = mpc;
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
     * Loads and displays the score board, by connecting to the database.
     */
    @FXML
    private void loadScoreBoard() {

        try{
            // Connecting to the database.
            Connection con = MorpionSolitaireController.connectToDataBase();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Player ORDER BY score DESC");

            ListView<String> scoreListView = new ListView<>();

            // Get the information from the database.
            while (rs.next()) {
                String playerName = rs.getString(2);
                int score = rs.getInt(3);
                String Mode=rs.getString(4);

                scoreListView.getItems().add(" Player: " + playerName + " | Score: " + score + " | Mode: "+ Mode);
            }

            // Close the connection.
            con.close();

            // Launch the scoreBoard scene.
            VBox root = new VBox(scoreListView);
            Scene scene = new Scene(root, 400, 300);
            Stage scoreBoard = new Stage();
            scoreBoard.setTitle("Scoreboard");
            scoreBoard.setScene(scene);

            scoreBoard.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when the game mode is changed.
     */
    @FXML
    private void gameModeChanged() {
        selectedMode = gameModeOptions.getSelectionModel().getSelectedItem();

        if (("5D".equals(selectedMode) || "5T".equals(selectedMode)))
            this.mode = selectedMode;
        else if (("5D#".equals(selectedMode)))
            this.mode ="5D";
        else if (("5T#".equals(selectedMode)))
            this.mode ="5T";
    }

    /**
     * Sets up the start options. The gameModeOptions ComboBox is updated, as the mode.
     */
    private void setupOptions() {
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5D", "5T","5D#","5T#");
        gameModeOptions.getSelectionModel().select("5D");
        selectedMode = "5D";
        this.mode = "5D";
    }

    public void start() {
        setupOptions();
    }

    /**
     * Shows an alert if the username is not filled.
     */
    private void showAlertUsername() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please enter your username.");
        alert.showAndWait();
    }
}