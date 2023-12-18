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
import java.sql.DriverManager;
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
     * Loads and displays the score board.
     */

    @FXML
    private void loadScoreBoard() {
        //TODO
        // Connecting to the database
        System.out.println("Connecting to the database...");

        try{

            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Replace the connection URL, username, and password with your own
            String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11671276";
            String username = "sql11671276";
            String password = "SKRsyy1vJr";

            // Establish the database connection
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");

            // Create a statement
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Player ORDER BY score DESC");

            // Create a ListView to display scores
            ListView<String> scoreListView = new ListView<>();

            // Populate the ListView with scores from the database
            while (rs.next()) {
                String playerName = rs.getString(2);
                int score = rs.getInt(3);
                String Mode=rs.getString(4);

                // Display the score in the ListView
                scoreListView.getItems().add(" Player: " + playerName + " | Score: " + score + " | Mode: "+ Mode);
            }

            // Close the database connection
            con.close();

            // Create a VBox to hold the ListView
            VBox root = new VBox(scoreListView);

            // Create the scene
            Scene scene = new Scene(root, 400, 300);

            // Create the new stage
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Scoreboard");
            secondaryStage.setScene(scene);

            // Show the stage
            secondaryStage.show();
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
        System.out.println("Selected Mode: " + selectedMode);

        if (("5D".equals(selectedMode) || "5T".equals(selectedMode))) {
            this.mode = selectedMode;
            System.out.println("Updated Mode: " + mode);
        }
        else if (("5DTsunami".equals(selectedMode))) {
            this.mode ="5D";
            System.out.println("Updated Mode: " + mode);
        }
        else if (("5TTsunami".equals(selectedMode))) {
            this.mode ="5T";
            System.out.println("Updated Mode: " + mode);
        }
    }

    /**
     * Set up the start options. The gameModeOptions ComboBox is updated,as the mode.
     */
    private void setupOptions() {
        gameModeOptions.getItems().removeAll(gameModeOptions.getItems());
        gameModeOptions.getItems().addAll("5D", "5T","5DTsunami","5TTsunami");
        gameModeOptions.getSelectionModel().select("5D");
        selectedMode = "5D";
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