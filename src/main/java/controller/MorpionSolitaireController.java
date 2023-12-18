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
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;


public class MorpionSolitaireController {
    public Canvas gameCanvas;
    public Label scoreLabel;
    public Label playerLabel;
    public Label modeLabel;
    public Button undoButton;
    public Button randomButton;
    public Button NMCSButton;
    public Button quitButton;
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

    public void start() {
        displayName(player);
        displayScore();
        displayMode(mode);
        drawBoard();
    }

    /**
     * Draw the entire board, with the grid and the points (played or not, and highlighted).
     */
    public void drawBoard() {
        ViewMorpionSolitaire.drawBoard(gameCanvas.getGraphicsContext2D(),gm.getBoard());
        displayScore();

        if (isGameOver()) {
            System.out.println("Game over !");
            //TODO : performGameOver();
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
        setColorMouseEnteredGame(quitButton);
        setColorMouseEnteredGame(hintButton);
    }

    /**
     * Sets the mouse-entered and mouse-exited styles for a button, changing its background color.
     * It is specific to the home page.
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
        playerLabel.setText("Player: " + player.getName());
    }

    public void displayMode(String mode){
        modeLabel.setText("Mode: " + mode);
    }

    /**
     * Undo the last player move.
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
     * Quit the game, to get back to the home page.
     */
    @FXML
    private void quit() {
        try {
            showAlertQuitTheGame();
        } catch (IOException e) {
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
     * Displays a confirmation alert to inquire if the player is certain about quitting the game.
     * @throws IOException Thrown if there is an issue loading the home-view.fxml file.
     */
    private void showAlertQuitTheGame() throws IOException {
        Alert alert = createQuitGameConfirmationAlert();

        // Show the alert and wait for user input.
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked "Yes".
        if (result.isPresent() && result.get() == ButtonType.YES) {
            initiateQuitGameProcess();
        }
    }

    /**
     * Create a confirmation alert for quitting the game.
     * @return The created confirmation alert.
     */
    private Alert createQuitGameConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to quit the game? You will be sent back to the home screen");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Set up the "Yes" button event handler
        Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
        if (yesButton != null) {
            yesButton.setOnAction(event -> initiateQuitGameProcess());
        }

        return alert;
    }

    /**
     * Initiate the quit game process.
     * @throws RuntimeException Thrown if there is an issue loading the home-view.fxml file.
     */
    private void initiateQuitGameProcess() {
        ViewMorpionSolitaire.highlightPoints.clear();
        hintActivated = false;

        // Close the current game window.
        Stage currentStage = (Stage) gameCanvas.getScene().getWindow();
        currentStage.close();

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
}
