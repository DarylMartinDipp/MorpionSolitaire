package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Board;
import model.Line;
import model.Point;

import java.util.ArrayList;
import java.util.List;

public class ViewMorpionSolitaire {
    private static final double OBJECTS_WIDTH = 1.0;
    private static final int WIDTH = GameManager.DIMENSION * 20;
    private static final int HEIGHT = GameManager.DIMENSION * 20;

    protected static List<Point> clickablePoints = new ArrayList<>();
    protected static List<Point> highlightPoints = new ArrayList<>();

    /**
     * Draws the Morpion Solitaire game board.
     * This includes the background color, grid lines, clickable points, placed points, and highlighted points.
     * @param gc The GraphicsContext on which to draw the board.
     * @param board The Board object representing the game state.
     */
    protected static void drawBoard(GraphicsContext gc, Board board) {
        // Set background color
        gc.setFill(Color.web("#6170ba"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Utilisez la méthode drawGrid avec le contexte graphique
        drawGrid(gc);

        // Draw all points on the grid and make them clickable.
        for (int i = 0; i < GameManager.DIMENSION; i++) {
            for (int j = 0; j < GameManager.DIMENSION; j++) {
                int x = i * (WIDTH / GameManager.DIMENSION);
                int y = j * (HEIGHT / GameManager.DIMENSION);

                drawClickablePoint(gc, x, y);
                clickablePoints.add(new Point(x, y));
            }
        }

        // Draw all the placed points.
        for (Point point : board.getPointsPlaced()) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY()* (HEIGHT / GameManager.DIMENSION);

            drawPlacedPoint(gc, x, y);
        }

        // Draw lines for the points in the board.
                drawLines(gc, board);
        // Draw all scored points
        int position = 1; // Initialiser le compteur à 1

        for (Point point : board.getLastAddedPoints()) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY() * (HEIGHT / GameManager.DIMENSION);

            drawScoredPoint(gc, x, y, position);
            position++; // Incrémenter le compteur pour la prochaine itération
        }


        // Draw all highlighted points if any.
        for (Point point : highlightPoints) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY()* (HEIGHT / GameManager.DIMENSION);

            drawHighlightedPoint(gc, x, y);
        }
    }

    /**
     * Draws the grid on the canvas.
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    private static void drawGrid(GraphicsContext gc) {
        // Draw horizontal lines
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            int y = i * (HEIGHT / GameManager.DIMENSION);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(0, y, WIDTH, y);
        }

        // Draw vertical lines
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            int x = i * (WIDTH / GameManager.DIMENSION);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(x, 0, x, HEIGHT);
        }
    }

    private static void drawClickablePoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 12.5; // Changer la taille des ovales
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.rgb(144, 238, 144, 0.0)); // Couleur avec une composante alpha de 0.0 (totalement transparente)
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
    }

    private static void drawPlacedPoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 12.5; // Augmenter la taille des ovales
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.LIGHTSALMON);
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
    }

    private static void drawScoredPoint(GraphicsContext gc, double x, double y, int position) {
        double ovalSize = OBJECTS_WIDTH * 12.5;
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.LIGHTSALMON);
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
        gc.setFill(Color.BLACK); // Couleur du texte
        gc.fillText(String.valueOf(position), x - halfSize + 2, y + halfSize - 2);
    }

    private static void drawHighlightedPoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 12; // Augmenter la taille des ovales
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.RED);
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
    }

    private static void drawLines(GraphicsContext gc, Board board) {
        gc.setStroke(Color.WHITE); // Couleur de la ligne

        for (Line line : board.getLines()) {
            ArrayList<Point> pointsOfTheLine = line.getPointsOfTheLine();

            if (pointsOfTheLine.size() > 1) {
                // Commencer le dessin de la ligne à partir du premier point
                Point startPoint = pointsOfTheLine.get(0);
                int startX = startPoint.getX() * (WIDTH / GameManager.DIMENSION);
                int startY = startPoint.getY() * (HEIGHT / GameManager.DIMENSION);
                gc.beginPath();
                gc.moveTo(startX, startY);

                // Ajouter des segments de ligne pour les points suivants
                for (int i = 1; i < pointsOfTheLine.size(); i++) {
                    Point point = pointsOfTheLine.get(i);
                    int x = point.getX() * (WIDTH / GameManager.DIMENSION);
                    int y = point.getY() * (HEIGHT / GameManager.DIMENSION);
                    gc.lineTo(x, y);
                }

                gc.stroke(); // Dessiner la ligne
            }
        }
    }

    /**
     * Handles mouse-click events on the Morpion Solitaire game canvas.
     * This method is called if the game aren't waiting confirmation of the point to play in 5T mode.
     * Translates the clicked coordinates to the corresponding grid point,
     * then delegates the handling to the board by asking it to process the clicked point.
     * @param event The MouseEvent triggered by the user's mouse click.
     * @param gameManagerWithBoard The GameManager instance containing the game board.
     * @param gameCanvas The Canvas on which the game board is displayed.
     */
    protected static void handleClick(MouseEvent event, GameManager gameManagerWithBoard, Canvas gameCanvas) {
        double clickedX = event.getX();
        double clickedY = event.getY();

        // Check if the click coordinates are within the bounds of any clickable point.
        for (Point point : clickablePoints) {
            double pointX = point.getX();
            double pointY = point.getY();

            if (Math.pow(clickedX - pointX, 2) + Math.pow(clickedY - pointY, 2) <= Math.pow(5.0, 2)) {
                int translatedX = coordinateTranslatorX(pointX);
                int translatedY = coordinateTranslatorY(pointY);

                System.out.println("Click at the position of the point: (x=" + translatedX + ", y=" + translatedY + ")");

                gameManagerWithBoard.getBoard().askPoint(translatedX, translatedY);
                drawBoard(gameCanvas.getGraphicsContext2D(), gameManagerWithBoard.getBoard());

                break;
            }
        }
    }

    /**
     * Handles mouse-click events for the 5T game mode, when the user need to choose a point between two.
     * @param event The MouseEvent triggered by the user's mouse click.
     * @return The Point on the grid corresponding to the clicked coordinates, or null if no matching point is found.
     */
    protected static Point handle5TChoiceClick(MouseEvent event) {
        double clickedX = event.getX();
        double clickedY = event.getY();

        // Check if the click coordinates are within the bounds of any clickable point.
        for (Point point : clickablePoints) {
            double pointX = point.getX();
            double pointY = point.getY();

            if (Math.pow(clickedX - pointX, 2) + Math.pow(clickedY - pointY, 2) <= Math.pow(5.0, 2)) {
                int translatedX = coordinateTranslatorX(pointX);
                int translatedY = coordinateTranslatorY(pointY);

                return new Point(translatedX, translatedY);
            }
        }
        return null;
    }

    /**
     * Translates a coordinate from the grid space to the corresponding X-coordinate on the game board.
     * @param gridX The X-coordinate in the grid space.
     * @return The translated X-coordinate on the game board.
     */
    private static int coordinateTranslatorX(double gridX) {
        return (int) (gridX / (WIDTH / GameManager.DIMENSION));
    }

    /**
     * Translates a coordinate from the grid space to the corresponding Y-coordinate on the game board.
     * @param gridY The Y-coordinate in the grid space.
     * @return The translated Y-coordinate on the game board.
     */
    private static int coordinateTranslatorY(double gridY) {
        return (int) (gridY / (HEIGHT / GameManager.DIMENSION));
    }

}
