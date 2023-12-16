package controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Board;
import model.Point;

import java.util.ArrayList;
import java.util.List;

public class ViewMorpionSolitaire {
    private static final double OBJECTS_WIDTH = 1.0;
    public static final int WIDTH = GameManager.DIMENSION * 20;
    public static final int HEIGHT = GameManager.DIMENSION * 20;

    static List<Point> clickablePoints = new ArrayList<>();

    public static void drawBoard(GraphicsContext gc, Board board) {
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
    }

    private static void drawClickablePoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.LIGHTGREEN);
        gc.fillOval(x, y, OBJECTS_WIDTH * 4, OBJECTS_WIDTH * 4);
    }

    private static void drawPlacedPoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.LIGHTSALMON);
        gc.fillOval(x, y, OBJECTS_WIDTH * 4, OBJECTS_WIDTH * 4);
    }

    private static void drawGrid(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setStroke(Color.LIGHTBLUE);

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

    static void handleCanvasClick(MouseEvent event) {
        double clickedX = event.getX();
        double clickedY = event.getY();

        // Check if the click coordinates are within the bounds of any clickable point
        for (Point point : clickablePoints) {
            double pointX = point.getX();
            double pointY = point.getY();
            double radius = 5.0;

            if (Math.pow(clickedX - pointX, 2) + Math.pow(clickedY - pointY, 2) <= Math.pow(radius, 2)) {
                int translatedX = coordinateTranslatorX(pointX);
                int translatedY = coordinateTranslatorY(pointY);

                System.out.println("Click at the position of the point: (x=" + translatedX + ", y=" + translatedY + ")");
                break;
            }
        }
    }

    private static int coordinateTranslatorX(double gridX) {
        return (int) (gridX / (WIDTH / GameManager.DIMENSION));
    }

    private static int coordinateTranslatorY(double gridY) {
        return (int) (gridY / (HEIGHT / GameManager.DIMENSION));
    }

}
