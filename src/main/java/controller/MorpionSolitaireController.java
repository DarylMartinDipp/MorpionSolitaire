package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.Player;

public class MorpionSolitaireController {
    @FXML
    public Label scoreLabel;
    @FXML
    public Label playerLabel;
    @FXML
    public Label modeLabel;
    public Button undoButton;
    public Button simulateButton;
    public Button restartButton;
    public Button hintButton;

    private GameManager gm;

    private Player player;
    private String mode;



    @FXML
    private Canvas gameCanvas;

    public void start() {

        displayName(player);
        displayScore(player);
        displayMode(mode);
        drawGrid();
    }

    public void drawGrid() {
        ViewMorpionSolitaire.drawBoard(gameCanvas.getGraphicsContext2D(),gm.getBoard());
    }

    @FXML
    private void canvasMousePressed(MouseEvent me) {
        gameCanvas.setOnMouseClicked(ViewMorpionSolitaire::handleCanvasClick);
    }

    @FXML
    private void initialize() {
        setColorMouseEnteredGame(undoButton);
        setColorMouseEnteredGame(simulateButton);
        setColorMouseEnteredGame(restartButton);
        setColorMouseEnteredGame(hintButton);
    }

    static void setColorMouseEnteredGame(Button button) {
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: lightblue;");
        });

        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #6170ba;"));
    }
    public void displayScore(Player player){
        scoreLabel.setText("Score: "+player.getScore());
    }

    public void displayName(Player player){
        playerLabel.setText("Player: "+player.getName());
    }

    public void displayMode(String mode){
        modeLabel.setText("Mode: "+mode);
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


}