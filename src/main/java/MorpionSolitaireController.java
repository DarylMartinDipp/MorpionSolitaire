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
        //    private Grid grid;
        private GamesCanvasView canvasView;

        private static final double CELL_SIZE = 30; // Taille de chaque cellule
        private static final double LINE_WIDTH = 1; // Largeur de la ligne entre les cellules

        String username;



        @FXML
        private void canvasMousePressed(MouseEvent me) {
            double mouseX = me.getX();
            double mouseY = me.getY();



            gameCanvas.setOnMouseClicked(e -> GamesCanvasView.handleCanvasClick(e));
                }







            public void start() {
            drawGrid();

        }

        private void setupOptions() {

        }

        @FXML
        private void playerChanged() {

        }

        @FXML
        private void reset() {

        }


        public void drawGrid() {
            GamesCanvasView.drawBoard(gameCanvas.getGraphicsContext2D(), GameManager.getBoard(),gameCanvas);
        }








    }