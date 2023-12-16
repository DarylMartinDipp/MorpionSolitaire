package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MorpionSolitaireController {
    @FXML
    private TextField usernameField;

    @FXML
    private Label scoreLabel;

    @FXML
    private ComboBox<String> playerOptions;

    @FXML
    private ComboBox<String> algorithmOptions;

    @FXML
    private Button simulateButton;

    @FXML
    private Button hintButton;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private void canvasMousePressed(MouseEvent me) {
        gameCanvas.setOnMouseClicked(ViewMorpionSolitaire::handleCanvasClick);
    }

    public void start() {
        drawGrid();
    }

    @FXML
    private void playerChanged() {

    }

    @FXML
    private void reset() {

    }

    public void drawGrid() {
        ViewMorpionSolitaire.drawBoard(gameCanvas.getGraphicsContext2D(), GameManager.getBoard());
    }
}