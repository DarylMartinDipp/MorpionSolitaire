package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MorpionSolitaireController {
    public Label scoreLabel;
    public Button undoButton;
    public Button simulateButton;
    public Button restartButton;
    public Button hintButton;

    @FXML
    private Canvas gameCanvas;

    public void start() {
        drawGrid();
    }

    public void drawGrid() {
        ViewMorpionSolitaire.drawBoard(gameCanvas.getGraphicsContext2D(), GameManager.getBoard());
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
}