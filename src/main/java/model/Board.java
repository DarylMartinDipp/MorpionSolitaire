package model;

import controller.GameManager;
import java.util.ArrayList;

import static controller.HomeController.selectedMode;

/**
 * The game board, containing the points and the lines.
 */
public abstract class Board {
    private final ArrayList<Point> pointsPlaced;
    private int score;
    private final ArrayList<Line> lines;

    private final ArrayList<Point> pointsAddedByUser = new ArrayList<>();

    public Board() {
        this.pointsPlaced = new ArrayList<>();
        this.score = 0;
        this.lines = new ArrayList<>();
        // Initialize the board with the right pattern, Cross or Tsunami
        initBoard(selectedMode);
    }

    /**
     * Initialization of the board, by initializing the points of the cross or of the tsunami, depending on the chosen mode.
     * @param mode The mode to use for the board, Cross ("5D" and "5T") or Tsunami ("5DTsunami" or "5TTsunami").
     */
    public void initBoard(String mode) {
        if ("5T".equals(mode) || "5D".equals(mode))
            initCross();
        else
            initTsunami();
    }

    /**
     * Initializing of the points of the cross.
     */
    public void initCross() {
        addPoint(3, 6);
        addPoint(3, 7);
        addPoint(3, 8);
        addPoint(3, 9);

        addPoint(4, 6);
        addPoint(4, 9);

        addPoint(5, 6);
        addPoint(5, 9);

        addPoint(6, 3);
        addPoint(6, 4);
        addPoint(6, 5);
        addPoint(6, 6);
        addPoint(6, 9);
        addPoint(6, 10);
        addPoint(6, 11);
        addPoint(6, 12);

        addPoint(7, 3);
        addPoint(7, 12);

        addPoint(8, 3);
        addPoint(8, 12);

        addPoint(9, 3);
        addPoint(9, 4);
        addPoint(9, 5);
        addPoint(9, 6);
        addPoint(9, 9);
        addPoint(9, 10);
        addPoint(9, 11);
        addPoint(9, 12);

        addPoint(10, 6);
        addPoint(10, 9);

        addPoint(11, 6);
        addPoint(11, 9);

        addPoint(12, 6);
        addPoint(12, 7);
        addPoint(12, 8);
        addPoint(12, 9);
    }

    /**
     * Initializing of the points of the tsunami.
     */
    public void initTsunami() {
        // Arrow points

        addPoint(2,7);

        addPoint(4, 4);
        addPoint(5, 4);


        addPoint(3, 5);
        addPoint(4, 5);
        addPoint(5, 5);


        addPoint(2, 6);
        addPoint(3, 6);
        addPoint(4, 6);

        addPoint(3, 7);
        addPoint(4, 7);
        addPoint(6, 7);
        addPoint(7, 7);

        addPoint(4, 8);


        addPoint(5, 9);

        // Additional specified points
        addPoint(1, 7);
        addPoint(2, 6);
        addPoint(3, 5);
        addPoint(4, 4);
        addPoint(2, 8);
        addPoint(3, 9);
        addPoint(4, 10);
        addPoint(3,8);
        addPoint(4,9);
        addPoint(5,10);
        addPoint(10,7);
        addPoint(11,7);
        addPoint(12,7);
        addPoint(14,7);
        addPoint(8,7);
    }

    /**
     * Adds a point to the collection of placed points on the game grid.
     * @param x The x-coordinate of the point to be added.
     * @param y The y-coordinate of the point to be added.
     */
    void addPoint(int x, int y) {
        Point pointToAdd = new Point(x, y);

        // Check if the point is not already present in the collection of placed points.
        if (!pointsPlaced.contains(pointToAdd))
            pointsPlaced.add(pointToAdd);
    }

    /**
     * Ask a point, to know if the point can be placed or not.
     * @param x The X-coordinate of the point to be added.
     * @param y The Y-coordinate of the point to be added.
     */
    public void askPoint(int x, int y) {
        // Here, the point entered is valid, so it is created.
        Point pointToAdd = new Point(x, y);

        // Check if the point already exists.
        if (!pointsPlaced.contains(pointToAdd) && canPointBePlayed(pointToAdd, true)) {
            playPoint(x, y);
            pointsAddedByUser.add(new Point(x, y));
            score++;
        }
    }

    /**
     * Check if the point can be placed here.
     * @param pointToPlay the point to be placed.
     * @param hasToPlay True if the points hs to be played, false otherwise.
     *                  False is for having all the playable points.
     * @return True if the point can be placed, false otherwise.
     */
    public abstract boolean canPointBePlayed(Point pointToPlay, boolean hasToPlay);

    /**
     * Check if there is a potential alignment of 5 points in the specified direction starting from the given coordinates.
     * @param x The X-coordinate to start checking from.
     * @param y The Y-coordinate to start checking from.
     * @param direction The direction in which to check for alignment (HORIZONTAL, VERTICAL, B_DIAGONAL, T_DIAGONAL).
     * @param addLineDirectly If true, a line is directlyAdded (for 5D), false otherwise (for 5T)
     * @return True if an alignment is found and a line is added; otherwise, false.
     */
    protected boolean hasAlignmentInDirection(int x, int y, Direction direction, boolean addLineDirectly) {
        int[] offset = getOffset(direction);
        int dx = offset[0];
        int dy = offset[1];

        // Create a list to store points aligned in the specified direction.
        ArrayList<Point> pointsAligned = new ArrayList<>();
        for (int i = -4; i <= 4; i++) {
            if (i == 0) {
                pointsAligned.add(new Point(x,y));

                // If the list size reaches 5, add a line (only for 5D) and return true.
                if (pointsAligned.size() == 5) {
                    if (addLineDirectly)
                        addLine(pointsAligned, direction);
                    return true;
                }

                continue;
            }

            // Calculate the current coordinates based on the direction and index.
            int currentX = x + i * dx;
            int currentY = y + i * dy;

            // Skip points that are outside the grid bounds.
            if (currentX < 0 || currentX >= GameManager.DIMENSION || currentY < 0 || currentY >= GameManager.DIMENSION)
                continue;

            Point point = new Point(currentX, currentY);

            // Check if the point is placed on the grid and not part of an existing line in the specified direction.
            if (pointsPlaced.contains(point) && !isPointPartOfLine(point, direction))
                pointsAligned.add(point);
            else
                pointsAligned.clear();

            // If the list size reaches 5, add a line (only for 5D) and return true.
            if (pointsAligned.size() == 5) {
                if (addLineDirectly)
                    addLine(pointsAligned, direction);
                return true;
            }
        }

        return false;
    }

    /**
     * Determines the offset (change in coordinates) based on the specified direction.
     * @param direction The direction for which to determine the offset.
     * @return An array representing the offset where index 0 is the x-coordinate offset and index 1 is the y-coordinate offset.
     */
    int[] getOffset(Direction direction) {
        // Initialize the offset array with default values.
        int[] offset = {0, 0};

        // Determine the change in coordinates based on the specified direction.
        switch (direction) {
            case HORIZONTAL -> offset[0] = 1; // Move one step to the right for horizontal direction.
            case VERTICAL -> offset[1] = 1;   // Move one step down for vertical direction.
            case B_DIAGONAL -> {
                // Move one step to the left and one step down for bottom diagonal direction.
                offset[0] = -1;
                offset[1] = 1;
            }
            case T_DIAGONAL -> {
                // Move one step to the right and one step down for top diagonal direction.
                offset[0] = 1;
                offset[1] = 1;
            }
        }

        // Return the determined offset.
        return offset;
    }

    /**
     * Checks if a given point is part of any existing line in the specified direction on the game grid.
     * @param pointToVerify The point to check for inclusion in a line.
     * @param direction The direction in which to check for the existence of a line (HORIZONTAL, VERTICAL, B_DIAGONAL, T_DIAGONAL).
     * @return True if the point is part of an existing line in the specified direction; otherwise, false.
     */
    protected boolean isPointPartOfLine(Point pointToVerify, Direction direction) {
        for (Line line : lines) {
            if (line.getDirection() == direction) {
                if (line.getPointsOfTheLine().contains(pointToVerify))
                    return true;
            }
        }

        return false;
    }

    /**
     * Play the point as in the mode.
     * @param x The X-coordinate of the point to be added.
     * @param y The Y-coordinate of the point to be added.
     */
    protected abstract void playPoint(int x, int y);

    /**
     * Processes the user's choice in the 5T game mode based on the selected point to play.
     * THIS METHOD IS ONLY USEFUL IN BOARD5T.
     * @param userPointChoice The chosen point to play on the game board.
     */
    public abstract void process5TUserChoice(Point userPointChoice);

    /**
     * Adds a new line to the list of lines on the game grid based on the given points and direction.
     * @param pointsOfNewLine The list of points forming the new line.
     * @param directionOfNewLine The direction of the new line (HORIZONTAL, VERTICAL, B_DIAGONAL, T_DIAGONAL).
     */
    public void addLine(ArrayList<Point> pointsOfNewLine, Direction directionOfNewLine) {
        Line newLine = new Line(pointsOfNewLine, directionOfNewLine);
        lines.add(newLine);
    }

    public ArrayList<Point> getPointsPlaced() {
        return pointsPlaced;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Point> getPointsAddedByUser() {
        return pointsAddedByUser;
    }
}
