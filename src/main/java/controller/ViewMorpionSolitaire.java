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

    protected static MorpionSolitaireController hintController;

    /**
     * Draws the Morpion Solitaire game board entirely.
     * @param gc The GraphicsContext on which to draw the board.
     * @param board The Board object representing the game state.
     */
    protected static void drawBoard(GraphicsContext gc, Board board) {
        drawBackgroundColor(gc);
        drawGrid(gc);
        drawClickablePoints(gc);
        drawPlacedPoints(gc, board);
        drawHintPoints(gc);
        drawLines(gc, board);
        drawScoredPoints(gc, board.getPointsAddedByUser());
        drawHighlightedPoints(gc);
    }

    /**
     * Sets the background color of the game board.
     * @param gc The GraphicsContext on which to draw the background color.
     */
    private static void drawBackgroundColor(GraphicsContext gc) {
        gc.setFill(Color.web("#6170ba"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * Draws the grid of the game board.
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    private static void drawGrid(GraphicsContext gc) {
        // Draw horizontal lines.
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            int y = i * (HEIGHT / GameManager.DIMENSION);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(0, y, WIDTH, y);
        }

        // Draw vertical lines.
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            int x = i * (WIDTH / GameManager.DIMENSION);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(x, 0, x, HEIGHT);
        }
    }

    /**
     * Draws all clickable points on the game board.
     * @param gc The GraphicsContext on which to draw the clickable points.
     */
    private static void drawClickablePoints(GraphicsContext gc) {
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            for (int j = 0; j <= GameManager.DIMENSION; j++) {
                int x = i * (WIDTH / GameManager.DIMENSION);
                int y = j * (HEIGHT / GameManager.DIMENSION);

                drawClickablePoint(gc, x, y);
                clickablePoints.add(new Point(x, y));
            }
        }
    }

    private static void drawClickablePoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 10; // Changer la taille des ovales
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.rgb(144, 238, 144, 0.0)); // Couleur avec une composante alpha de 0.0 (totalement transparente)
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
    }

    /**
     * Draws all placed points on the game board.
     * @param gc The GraphicsContext on which to draw the placed points.
     * @param board The game board.
     */
    private static void drawPlacedPoints(GraphicsContext gc, Board board) {
        for (Point point : board.getPointsPlaced()) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY() * (HEIGHT / GameManager.DIMENSION);

            drawPlacedPoint(gc, x, y);
        }
    }

    private static void drawPlacedPoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 10; // Augmenter la taille des ovales
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
    }

    /**
     * Draws all hint points on the game board if hint functionality is activated.
     * @param gc The GraphicsContext on which to draw the hint points.
     */
    private static void drawHintPoints(GraphicsContext gc) {
        if (MorpionSolitaireController.hintActivated) {
            ArrayList<Point> hintPoints = hintController.searchAllPlayablePoints();

            for (Point hintPoint : hintPoints) {
                int x = hintPoint.getX() * (WIDTH / GameManager.DIMENSION);
                int y = hintPoint.getY() * (HEIGHT / GameManager.DIMENSION);

                drawHintPoint(gc, x, y);
            }
        }
    }

    private static void drawHintPoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 10;
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.rgb(0, 0, 139, 0.5));

        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
    }

    /**
     * Draws all lines on the game board.
     * @param gc The GraphicsContext on which to draw the placed points.
     * @param board The game board.
     */
    private static void drawLines(GraphicsContext gc, Board board) {
        gc.setStroke(Color.WHITE);

        // Iterate through all the lines on the board.
        for (Line line : board.getLines()) {
            ArrayList<Point> pointsOfTheLine = line.getPointsOfTheLine();

            if (pointsOfTheLine.size() > 1) {
                // Get the starting point of the line (the first point in the sorted list).
                Point startPoint = pointsOfTheLine.get(0);
                int startX = startPoint.getX() * (WIDTH / GameManager.DIMENSION);
                int startY = startPoint.getY() * (HEIGHT / GameManager.DIMENSION);
                gc.beginPath();
                gc.moveTo(startX, startY);

                // Add line segments for the remaining points
                for (int i = 1; i < pointsOfTheLine.size(); i++) {
                    Point point = pointsOfTheLine.get(i);
                    int x = point.getX() * (WIDTH / GameManager.DIMENSION);
                    int y = point.getY() * (HEIGHT / GameManager.DIMENSION);
                    gc.lineTo(x, y);
                }

                // Draw the line.
                gc.stroke();
            }
        }
    }

    /**
     * Draws each point added by the user, with the right number on it.
     * @param gc The GraphicsContext on which to draw the point numbers.
     * @param points The list of points added by the user.
     */
    private static void drawScoredPoints(GraphicsContext gc, List<Point> points) {
        int pointNumber = 1;

        for (Point point : points) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY() * (HEIGHT / GameManager.DIMENSION);

            drawScoredPoint(gc, x, y, pointNumber);
            pointNumber++;
        }
    }

    private static void drawScoredPoint(GraphicsContext gc, double x, double y, int position) {
        double ovalSize = OBJECTS_WIDTH * 20;
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.DARKBLUE);
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
        gc.setFill(Color.WHITE);
        if (position < 10)
            gc.fillText(String.valueOf(position), x - halfSize + 6, y + halfSize - 6);
        else
            gc.fillText(String.valueOf(position), x - halfSize + 3, y + halfSize - 5);
    }

    /**
     * Draws all highlighted points on the game board.
     * @param gc The GraphicsContext on which to draw the highlighted points.
     */
    private static void drawHighlightedPoints(GraphicsContext gc) {
        for (Point point : highlightPoints) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY() * (HEIGHT / GameManager.DIMENSION);

            drawHighlightedPoint(gc, x, y);
        }
    }

    private static void drawHighlightedPoint(GraphicsContext gc, double x, double y) {
        double ovalSize = OBJECTS_WIDTH * 10; // Augmenter la taille des ovales
        double halfSize = ovalSize / 2.0;

        gc.setFill(Color.RED);
        gc.fillOval(x - halfSize, y - halfSize, ovalSize, ovalSize);
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

