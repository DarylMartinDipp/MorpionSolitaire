import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GamesCanvasView {

    private static final double LINE_WIDTH = 1.0; // Width of the lines
    static List<Point> clickablePoints = new ArrayList<>();

    // Adjust the grid dimensions
    private static final int GRID_DIMENSION = 16; // Change as needed

    // Adjust the canvas size
    public static final int WIDTH = GRID_DIMENSION * 20; // Adjust the multiplier as needed
    public static final int HEIGHT = GRID_DIMENSION * 20; // Adjust the multiplier as needed
    private static double y;
    private static double x;


    public static void drawBoard(GraphicsContext gc, Board board, Canvas canvas) {
        // Draw the grid
        drawGrid(gc);

        // Draw the orthogonal axes
        drawOrthogonalAxes(gc);

        // Draw all points on the grid and make them clickable
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                 x = i * (WIDTH / GRID_DIMENSION);
                 y = j * (HEIGHT / GRID_DIMENSION);

                drawClickablePoint(gc, x, y);
                clickablePoints.add(new Point((int) x, (int) y));
            }
        }

        // Draw placed points
        for (Point point : board.getPointsPlaced()) {
            double x = point.getX() * (WIDTH / GRID_DIMENSION);
            double y = point.getY()* (HEIGHT / GRID_DIMENSION);

            drawPlacedPoint(gc, x, y);
        }

        // Draw lines
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(LINE_WIDTH);

        for (Line line : board.getLines()) {
            double startX = line.getStartX() * (WIDTH / GRID_DIMENSION);
            double startY = line.getStartY() * (HEIGHT / GRID_DIMENSION);
            double endX = line.getEndX() * (WIDTH / GRID_DIMENSION);
            double endY = line.getEndY() * (HEIGHT / GRID_DIMENSION);

            gc.strokeLine(startX, startY, endX, endY);
        }
    }

    private static void drawClickablePoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.GREEN);
        gc.fillOval(x, y, LINE_WIDTH * 4, LINE_WIDTH * 4);
    }

    private static void drawPlacedPoint(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, LINE_WIDTH * 4, LINE_WIDTH * 4);
    }

    private static void drawGrid(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw horizontal lines
        for (int i = 0; i <= GRID_DIMENSION; i++) {
            double y = i * (HEIGHT / GRID_DIMENSION);
            gc.strokeLine(0, y, WIDTH, y);
        }

        // Draw vertical lines
        for (int i = 0; i <= GRID_DIMENSION; i++) {
            double x = i * (WIDTH / GRID_DIMENSION);
            gc.strokeLine(x, 0, x, HEIGHT);
        }
    }

    private static void drawOrthogonalAxes(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(LINE_WIDTH);

        // Draw the vertical axis (y)
        double centerX = WIDTH / 2;
        gc.strokeLine(centerX, 0, centerX, HEIGHT);

        // Draw the horizontal axis (x)
        double centerY = HEIGHT / 2;
        gc.strokeLine(0, centerY, WIDTH, centerY);
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
                // Handle the click event for the specific point (translatedX, translatedY)
                break; // Assuming only one point can be clicked at a time
            }
        }
    }

    private static int coordinateTranslatorX(double gridX) {
        // Adjust the x-coordinate based on the grid dimensions
        return (int) (gridX / (WIDTH / GRID_DIMENSION));
    }

    private static int coordinateTranslatorY(double gridY) {
        // Adjust the y-coordinate based on the grid dimensions
        return (int) (gridY / (HEIGHT / GRID_DIMENSION));
    }

}
