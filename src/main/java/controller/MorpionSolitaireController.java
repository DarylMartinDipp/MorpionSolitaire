package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.Player;
import model.Point;

public class MorpionSolitaireController {
    public Canvas gameCanvas;
    public Label scoreLabel;
    public Label playerLabel;
    public Label modeLabel;
    public Button undoButton;
    public Button simulateButton;
    public Button restartButton;
    public Button hintButton;

    private GameManager gm;
    private Player player;
    private String mode;

    // Those 3 attributes will be used to know if the player
    // needs to choose the point to play during 5T mode.
    private static boolean waitForUserChoice = false;
    protected static Point pointA;
    protected static Point pointB;

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

        // Draw the board and update the score.
        drawBoard();
        displayScore();
    }

    /**
     * Sets mouse-entered and mouse-exited styles for specific buttons.
     */
    @FXML
    private void initialize() {
        setColorMouseEnteredGame(undoButton);
        setColorMouseEnteredGame(simulateButton);
        setColorMouseEnteredGame(restartButton);
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

    @FXML
    private void undo() {
        //TODO
        System.out.println("The previous move should be canceled.");
    }

    @FXML
    private void restart() {
        //TODO
        System.out.println("The game should restart.");
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
}