package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Board;
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

        //Draw grid lines
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

        // Draw all highlighted points if any.
        for (Point point : highlightPoints) {
            int x = point.getX() * (WIDTH / GameManager.DIMENSION);
            int y = point.getY()* (HEIGHT / GameManager.DIMENSION);

            drawHighlightedPoint(gc, x, y);
        }
    }

    private static void drawGrid(GraphicsContext gc) {
        // Draw horizontal lines
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            int y = i * (HEIGHT / GameManager.DIMENSION);
            gc.strokeLine(0, y, WIDTH, y);
        }

        // Draw vertical lines
        for (int i = 0; i <= GameManager.DIMENSION; i++) {
            int x = i * (WIDTH / GameManager.DIMENSION);
            gc.strokeLine(x, 0, x, HEIGHT);
        }
    }

    private static void drawClickablePoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.LIGHTGREEN);
        gc.fillOval(x, y, OBJECTS_WIDTH * 4, OBJECTS_WIDTH * 4);
    }

    private static void drawPlacedPoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.LIGHTSALMON);
        gc.fillOval(x, y, OBJECTS_WIDTH * 4, OBJECTS_WIDTH * 4);
    }

    private static void drawHighlightedPoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, OBJECTS_WIDTH * 6, OBJECTS_WIDTH * 6);
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
