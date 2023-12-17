package model;

import controller.GameManager;
import controller.MorpionSolitaireController;

import java.util.ArrayList;

/**
 * The game board in the 5T mode.
 */
public class Board5T extends Board {
    private Direction dir;
    private final ArrayList<Point> upPoints = new ArrayList<>();
    private final ArrayList<Point> downPoints = new ArrayList<>();
    private ArrayList<Point> pointsForAFor5T = new ArrayList<>();
    private ArrayList<Point> pointsForBFor5T = new ArrayList<>();
    private int xFor5T;
    private int yFor5T;

    /**
     * Check if the point can be placed here.
     * @param pointToPlay the point to be placed.
     * @return True if the point can be placed, false otherwise.
     */
    @Override
    protected boolean canPointBePlayed(Point pointToPlay) {
        int x = pointToPlay.getX();
        int y = pointToPlay.getY();

        // Check for all directions if the point can be placed.
        for (Direction direction : Direction.values()) {
            if (hasAlignmentInDirection(x, y, direction, false)) {
                // Save the local variable "dir" to re-use it to play the point.
                dir = direction;
                return true;
            }
        }

        // Any alignment has been found in all direction, the point cannot be played.
        return false;
    }

    /**
     * Play the point as in the mode.
     * @param x The x-coordinate of the point to be added.
     * @param y The y-coordinate of the point to be added.
     */
    @Override
    protected void playPoint(int x, int y) {
        // Check if two lines can touch.
        if (!isAdjacentToAlignment(x, y, dir)) {
            // Redo hasAlignmentInDirection to save the line with addLine.
            hasAlignmentInDirection(x, y, dir, true);
            addPoint(x, y);
            System.out.println("Point successfully added.");
        }

        // Here, two lines can touch.
        else {
            // Check if we use the upPoint or the downPoint ArrayLists.
            if (isUpPoint())
                ask5TUserChoice(x, y, downPoints, upPoints);
            else
                ask5TUserChoice(x, y, upPoints, downPoints);
        }
    }

    /**
     * Checks if there is an alignment adjacent to the specified coordinates in the given direction.
     * @param x The x-coordinate to check for adjacency.
     * @param y The y-coordinate to check for adjacency.
     * @param direction The direction in which to check for adjacency (HORIZONTAL, VERTICAL, B_DIAGONAL, T_DIAGONAL).
     * @return True if there is an adjacent alignment; otherwise, false.
     */
    private boolean isAdjacentToAlignment(int x, int y, Direction direction) {
        int[] offset = getOffset(direction);
        int dx = offset[0];
        int dy = offset[1];

        // Check for adjacent alignment using the specified offset.
        return checkAdjacentAlignment(x, y, dx, dy);
    }

    /**
     * Checks for an alignment adjacent to the specified coordinates based on the given offset.
     * @param x The x-coordinate to start checking from.
     * @param y The y-coordinate to start checking from.
     * @param dx The change in x-coordinate to create an offset.
     * @param dy The change in y-coordinate to create an offset.
     * @return True if there is an adjacent alignment; otherwise, false.
     */
    private boolean checkAdjacentAlignment(int x, int y, int dx, int dy) {
        Point pointToCheck = new Point(x,y);

        // Clear the upPoints and downPoints ArrayLists and add the current point to both.
        // It uses the upPoint and downPoint ArrayLists to store the points above and below the current position.
        upPoints.clear();
        upPoints.add(pointToCheck);
        downPoints.clear();
        downPoints.add(pointToCheck);

        // Check points above and below the current position.
        for (int i = 1; i <= 4; i++) {
            int currentXUp = x - i * dx;
            int currentYUp = y - i * dy;

            int currentXDown = x + i * dx;
            int currentYDown = y + i * dy;

            // Check valid coordinates for the point above.
            if (isValidCoordinate(currentXUp, currentYUp)) {
                Point currentUpPoint = new Point(currentXUp, currentYUp);

                // Check if the point above is placed.
                if (pointsPlaced.contains(currentUpPoint)) {
                    upPoints.add(currentUpPoint);
                }
                else
                    break;
            }

            // Check valid coordinates for the point below.
            if (isValidCoordinate(currentXDown, currentYDown)) {
                Point currentDownPoint = new Point(currentXDown, currentYDown);

                // Check if the point below is placed.
                if (pointsPlaced.contains(currentDownPoint)) {
                    downPoints.add(currentDownPoint);
                }
                else
                    break;
            }
        }

        // If the combined size of upPoints and downPoints is greater than or equal to 10, the line can be D or T.
        return (upPoints.size() + downPoints.size()) >= 10;
    }

    /**
     * Checks if the specified coordinates are within the bounds of the game grid.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if the coordinates are valid; otherwise, false.
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < GameManager.DIMENSION && y >= 0 && y < GameManager.DIMENSION;
    }

    /**
     * Checks if the second point in the upPoints ArrayList is part of any existing line.
     * This is needed to know if the line can be drawn using the points in upPoint or downPoint ArrayLists.
     * @return True if the line can be drawn using the points in upPoint ArrayLists, false if the line can be drawn using the points in downPoint ArrayLists.
     */
    private boolean isUpPoint() {
        // Iterate through existing lines.
        for (Line line : lines) {
            if (line.getPointsOfTheLine().contains(upPoints.get(1)))
                // The point is part of an existing line: the points in downPoint can be used to draw the new line
                return false;
        }

        // The point is not part of any existing line: the points in upPoint can be used to draw the new line.
        return true;
    }

    /**
     * Processes the user's choice for connecting a point in a 5T game.
     * @param x The x-coordinate of the selected point.
     * @param y The y-coordinate of the selected point.
     * @param pointsForA ArrayList containing the points for option A.
     * @param pointsForB ArrayList containing the points for option B.
     */
    private void ask5TUserChoice(int x, int y, ArrayList<Point> pointsForA, ArrayList<Point> pointsForB) {
        System.out.println("A = " + pointsForA.get(1));
        System.out.println("B = " + pointsForB.get(0));

        this.pointsForAFor5T = pointsForA;
        this.pointsForBFor5T = pointsForB;
        this.xFor5T = x;
        this.yFor5T = y;

        MorpionSolitaireController.ask5TUserChoiceGraphic(pointsForA.get(1), pointsForB.get(0));
    }

    public void process5TUserChoice(Point pointToPlay) {
        if (pointToPlay.equals(pointsForAFor5T.get(1))) {
            addPoint(xFor5T, yFor5T);
            // Create an ArrayList for option A.
            ArrayList<Point> arrayListForA = new ArrayList<>();
            arrayListForA.add(pointsForAFor5T.get(1));
            arrayListForA.add(pointsForBFor5T.get(0));
            arrayListForA.add(pointsForBFor5T.get(1));
            arrayListForA.add(pointsForBFor5T.get(2));
            arrayListForA.add(pointsForBFor5T.get(3));
            // Add a line for option A.
            addLine(arrayListForA, dir);
            System.out.println("Point successfully added.");
        } else if (pointToPlay.equals(pointsForBFor5T.get(0))) {
            addPoint(xFor5T, yFor5T);
            // Add a line for option B.
            addLine(pointsForBFor5T, dir);
            System.out.println("Point successfully added.");
        }
    }
}
