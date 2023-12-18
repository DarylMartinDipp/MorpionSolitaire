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



public class MorpionSolitaireController {
    public Canvas gameCanvas;
    public Label scoreLabel;
    public Label playerLabel;
    public Label modeLabel;
    public Button undoButton;
    public Button simulateButton;
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
        setColorMouseEnteredGame(simulateButton);
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

        // Remove the last point from pointsAddedByUser
        gm.getBoard().getPointsAddedByUser().remove(gm.getBoard().getPointsAddedByUser().size() - 1);

        // Remove the last point from pointsPlaced
        gm.getBoard().getPointsPlaced().remove(gm.getBoard().getPointsPlaced().size() - 1);

        // Remove the last line
        gm.getBoard().getLines().remove(gm.getBoard().getLines().size() - 1);

        // Set the new score
        gm.getBoard().setScore(gm.getBoard().getScore() - 1);

        drawBoard();
    }


    /**
     * Toggles the display of a hint in the user interface.
     * If the hint is currently activated, it hides the hint; otherwise, it shows the hint.
     */
    @FXML
    public void performHint() {
        hintActivated = !hintActivated; // Inversion de la valeur de hintActivated
        drawBoard();
    }

    /**
     * Searches for all playable points on the game board.
     * @return An ArrayList containing all playable points on the game board.
     */
    private ArrayList<Point> searchAllPlayablePoints() {
        ArrayList<Point> allPlayablePoints = new ArrayList<>();

        // Loop through all points on the game board.
        for (int i = 0; i < GameManager.DIMENSION; i++) {
            for (int j = 0; j < GameManager.DIMENSION; j++) {
                Point pointToDefine = new Point(i,j);

                // Check if it can be played.
                // The 'false' parameter indicates that this is a hypothetical move
                if (gm.getBoard().canPointBePlayed(pointToDefine, false))
                    allPlayablePoints.add(pointToDefine);
            }
        }

        return allPlayablePoints;
    }

    @FXML
    private void quit() {
        //TODO: TextBox "Are you sure you want to quit?" "Yes" "No"
        // If No, stay on the screen
        // If Yes, go to Home page
        //MODIFICATION GUILLAUME
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
    private void showAlertQuitTheGame() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to quit the game? You will be sent back to home screen");

        // Ajouter des boutons personnalisés (Yes et No)
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Récupérer le bouton "Yes" et ajouter un gestionnaire d'événements
        Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
        yesButton.setOnAction(event -> {
            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) gameCanvas.getScene().getWindow(); // Remplacez gameCanvas par le composant que vous voulez utiliser
            currentStage.close();

            // Ouvrir la nouvelle fenêtre d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home-view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HomeController hc = loader.getController();
            hc.start();

            // Afficher la nouvelle fenêtre
            Stage newStage = new Stage();
            newStage.setTitle("Morpion Solitaire");
            newStage.setScene(new Scene(root));
            newStage.show();
        });

        alert.showAndWait();
    }

   //TODO MODIFICATION GUILLAUME
    public ArrayList<Point> getSearchAllPlayablePoints(){
        return searchAllPlayablePoints();
    }

}
