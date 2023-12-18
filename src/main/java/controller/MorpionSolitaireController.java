package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Player;
import model.Point;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static controller.HomeController.selectedMode;


public class MorpionSolitaireController {
    public Canvas gameCanvas;
    public Label scoreLabel;
    public Label playerLabel;
    public Label modeLabel;
    public Button undoButton;
    public Button randomButton;
    public Button goHomeScreenButton;
    public Button hintButton;

    private GameManager gm;
    private Player player;
    private String mode;

    // Those 3 attributes will be used to know if the player
    // needs to choose the point to play during 5T mode.
    private static boolean waitForUserChoice = false;
    protected static Point pointA;
    protected static Point pointB;

    public static boolean hintActivated;
    private String username;

    public void start() {
        displayName(player);
        displayScore();
        displayMode(mode);
        drawBoard();
    }

    /**
     * Draws the entire board, with the grid and the points (played or not, and highlighted).
     * Check if the game is over.
     */
    public void drawBoard() {
        ViewMorpionSolitaire.drawBoard(gameCanvas.getGraphicsContext2D(),gm.getBoard());
        displayScore();

        if (isGameOver()) {
            System.out.println("Game over !");
            performGameOver();
        }
    }

    /**
     * Handles mouse-pressed events on the canvas in the Morpion Solitaire game.
     * If waiting for user choice, captures the clicked point and processes a user choice in the 5T game mode.
     * Otherwise, delegates the handling to the ViewMorpionSolitaire class, updating the game board and view.
     * Finally, redraws the game board on the canvas.
     * @param me The MouseEvent triggered by the user's mouse press.
     */
    @FXML
    private void canvasMousePressed(MouseEvent me) {
        // If the game are waiting confirmation of the point to play in 5T mode.
        if (waitForUserChoice) {
            Point clickedPoint;
            clickedPoint = ViewMorpionSolitaire.handle5TChoiceClick(me);
            // If the point clicked if one of the expected points.
            if (clickedPoint != null && (clickedPoint.equals(pointA) || clickedPoint.equals(pointB))) {
                waitForUserChoice = false;
                // Process the user input.
                gm.getBoard().process5TUserChoice(clickedPoint);
                ViewMorpionSolitaire.highlightPoints.clear();
            }
        }

        // In all other cases.
        else
            ViewMorpionSolitaire.handleClick(me, gm, gameCanvas);

        // Draw the board.
        drawBoard();
    }

    /**
     * Sets mouse-entered and mouse-exited styles for specific buttons.
     */
    @FXML
    private void initialize() {
        setColorMouseEnteredGame(undoButton);
        setColorMouseEnteredGame(randomButton);
        setColorMouseEnteredGame(goHomeScreenButton);
        setColorMouseEnteredGame(hintButton);
    }

    /**
     * Sets the mouse-entered and mouse-exited styles for a button, changing its background color.
     * It is specific to the home screen.
     * @param button The Button object to which the styles are applied.
     */
    static void setColorMouseEnteredGame(Button button) {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: lightblue;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #6170ba;"));
    }

    public void displayScore(){
        scoreLabel.setText("Score: " + gm.getBoard().getScore());
    }

    public void displayName(Player player){
        username=player.getName();
        playerLabel.setText("Player: " + player.getName());
    }

    public void displayMode(String mode){
        modeLabel.setText("Mode: " + mode);
    }

    /**
     * Undoes the last player move.
     */
    @FXML
    private void undo() {
        if (gm.getBoard().getPointsAddedByUser().isEmpty())
            return;

        // Remove the last point from pointsAddedByUser.
        gm.getBoard().getPointsAddedByUser().remove(gm.getBoard().getPointsAddedByUser().size() - 1);

        // Remove the last point from pointsPlaced.
        gm.getBoard().getPointsPlaced().remove(gm.getBoard().getPointsPlaced().size() - 1);

        // Remove the last line.
        gm.getBoard().getLines().remove(gm.getBoard().getLines().size() - 1);

        // Set the new score.
        gm.getBoard().setScore(gm.getBoard().getScore() - 1);

        drawBoard();
    }

    /**
     * Toggles the hint functionality on or off.
     * Depending on the flag hintActivated, drawBoard() display the hint or not.
     */
    @FXML
    public void performHint() {
        hintActivated = !hintActivated;
        hintButton.setText(hintActivated ? "Hide Hint" : "Show Hint");
        drawBoard();
    }

    /**
     * Searches for all playable points on the game board.
     * @return An ArrayList containing all playable points on the game board.
     */
    public ArrayList<Point> searchAllPlayablePoints() {
        ArrayList<Point> allPlayablePoints = new ArrayList<>();

        // Loop through all points on the game board.
        for (int i = 0; i < GameManager.DIMENSION; i++) {
            for (int j = 0; j < GameManager.DIMENSION; j++) {
                Point pointToDefine = new Point(i,j);

                // Check if it can be played.
                // The 'false' parameter indicates that this is a hypothetical move
                if (gm.getBoard().canPointBePlayed(pointToDefine, false) && !gm.getBoard().getPointsPlaced().contains(pointToDefine))
                    allPlayablePoints.add(pointToDefine);
            }
        }

        return allPlayablePoints;
    }

    /**
     * Get back to the home screen.
     */
    @FXML
    private void goHomeScreen() {
        try {
            showAlertGoHomeScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Establishes a connection to a MySQL database.
     * @return A Connection object representing the established database connection.
     * @throws ClassNotFoundException If the MySQL JDBC driver class is not found.
     * @throws SQLException           If a database access error occurs or the URL is null.
     */
    public static Connection connectToDataBase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11671276";
        String user = "sql11671276";
        String password = "SKRsyy1vJr";

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Performs the necessary actions when the game is over, including connecting to the database,
     * inserting player information (name, score, mode) into the Player table, and displaying a game-over alert.
     * @throws RuntimeException If an IOException, SQLException, or ClassNotFoundException occurs during the process.
     */
    private void performGameOver(){
        try {
            // Connecting to the database.
            Connection con = connectToDataBase();
            Statement stmt = con.createStatement();
            System.out.println("Connected to the database.");

            // Insert the player's name and score into the Player table.
            String playerName = username;
            int score = gm.getBoard().getScore();
            String boardMode= selectedMode;

            String insertQuery = "INSERT INTO Player (Name, Score, Mode) VALUES ('" + playerName + "', " + score + ", '" + boardMode + "')";
            stmt.executeUpdate(insertQuery);

            // Close the connection.
            con.close();
            System.out.println("disconnected from Database");

            showAlertGameOver();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setGameManager(GameManager gameManager) {
        this.gm = gameManager;
    }

    public void setPlayer(Player player){
        this.player=player;
    }

    public void setMode(String mode){
        System.out.println(mode);
        this.mode=mode;
    }

    public void setName(String name){
        username=name + " (random)";
    }

    /**
     * Initiates the process of asking the user for their choice in the 5T game mode.
     * @param pointA The first user choice.
     * @param pointB The second user choice.
     */
    public static void ask5TUserChoiceGraphic(Point pointA, Point pointB) {
        // Set the user choices, to get them in Board5T.process5TUserChoice.
        MorpionSolitaireController.pointA = pointA;
        MorpionSolitaireController.pointB = pointB;

        // Add the user choices to the list of highlighted points in the view.
        ViewMorpionSolitaire.highlightPoints.add(pointA);
        ViewMorpionSolitaire.highlightPoints.add(pointB);

        // Flag that the system is waiting for the user's input.
        waitForUserChoice = true;
    }

    /**
     * Displays a confirmation alert to inquire if the player is certain about going to the home screen.
     * @throws IOException Thrown if there is an issue loading the home-view.fxml file.
     */
    private void showAlertGoHomeScreen() throws IOException {
        Alert alert = createGoHomeScreenConfirmationAlert();

        // Show the alert and wait for user input.
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked "Yes".
        if (result.isPresent() && result.get() == ButtonType.YES) {
            initiateGoHomeScreenProcess();
        }
    }

    /**
     * Creates a confirmation alert for going to the home screen.
     * @return The created confirmation alert.
     */
    private Alert createGoHomeScreenConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to quit the game? You will be sent back to the home screen.");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Set up the "Yes" button event handler
        Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
        if (yesButton != null) {
            yesButton.setOnAction(event -> initiateGoHomeScreenProcess());
        }

        return alert;
    }

    /**
     * Initiates the quit game process.
     * @throws RuntimeException Thrown if there is an issue loading the home-view.fxml file.
     */
    private void initiateGoHomeScreenProcess() {
        closeGame();

        // Open the new home screen window.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HomeController hc = loader.getController();
        hc.start();

        Stage newStage = new Stage();
        newStage.setTitle("Morpion Solitaire");
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    /**
     * Performs the random solver in the Morpion Solitaire game.
     */
    public void performRandom() {
        setName(username);
        do {
            ArrayList<Point> missingPoints = searchAllPlayablePoints();

            if (waitForUserChoice) {
                int randomNumberFor5T = new Random().nextInt(2) + 1;
                waitForUserChoice = false;

                if (randomNumberFor5T == 1) {
                    gm.getBoard().process5TUserChoice(pointA);
                }
                else
                    gm.getBoard().process5TUserChoice(pointB);

                ViewMorpionSolitaire.highlightPoints.clear();
            }

            // Play a random point in the missing points.
            if (!missingPoints.isEmpty()) {
                int randomIndex = new Random().nextInt(missingPoints.size());
                Point pointToPlayRandomly = missingPoints.get(randomIndex);

                gm.getBoard().askPoint(pointToPlayRandomly.getX(), pointToPlayRandomly.getY());
            }

            drawBoard();
        } while (!isGameOver());
    }

    /**
     * Checks if the Morpion Solitaire game is over.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        ArrayList<Point> missingPoints = searchAllPlayablePoints();
        return missingPoints.isEmpty();
    }

    /**
     * Displays a confirmation alert to inquire if the player is certain about quitting the game.
     * @throws IOException Thrown if there is an issue loading the home-view.fxml file.
     */
    private void showAlertGameOver() throws IOException {
        Alert alert = createGameOverConfirmationAlert();

        // Show the alert and wait for user input.
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked "Yes".
        if (result.isPresent() && result.get() == ButtonType.YES) {
            initiateGoHomeScreenProcess();
        }
    }

    /**
     * Creates a confirmation alert for quitting the game.
     * @return The created confirmation alert.
     */
    private Alert createGameOverConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);

        alert.setContentText("The game is over!\nName : "+username+"\nScore: "+gm.getBoard().getScore());

        ButtonType buttonTypeYes = new ButtonType("Go to home screen", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("Close the game", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Set up the "Yes" button event handler
        Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
        Button noButton = (Button) alert.getDialogPane().lookupButton(buttonTypeNo);

        if (yesButton != null && noButton!=null) {
            yesButton.setOnAction(event -> initiateGoHomeScreenProcess());
            noButton.setOnAction(event -> closeGame());

        }

        return alert;
    }

    /**
     * Closes the game.
     */
    public void closeGame(){
        ViewMorpionSolitaire.highlightPoints.clear();
        hintActivated = false;

        // Close the current game window.
        Stage currentStage = (Stage) gameCanvas.getScene().getWindow();
        currentStage.close();
    }
}
