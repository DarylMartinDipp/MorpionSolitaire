import java.util.ArrayList;
import java.util.Scanner;

/**
 * The game board, containing the points and the lines.
 */
public abstract class Board {
    protected ArrayList<Point> pointsPlaced;
    protected int score;
    protected ArrayList<Line> lines;

    public Board() {
        this.pointsPlaced = new ArrayList<>();
        this.score = 0;
        this.lines = new ArrayList<>();
        initBoard();
    }

    /**
     * Initialization of the board, by initializing the points of the cross.
     */
    public void initBoard() {
        initCross();
        //TODO: to delete after JavaFX
        displayBoard();
    }
    ArrayList initialPoints = new ArrayList<>();

    //TODO: to delete after JavaFX
    public void displayBoard() {
        for (int i = 0; i < GameManager.DIMENSION; i++) {
            for (int j = 0; j < GameManager.DIMENSION; j++) {
                if (pointsPlaced.contains(new Point(j,i)))
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

    /**
     * initializing of the points of the cross.
     */
    public void initCross() {
        addInitialPoint(3, 6);
        addInitialPoint(3, 7);
        addInitialPoint(3, 8);
        addInitialPoint(3, 9);

        addInitialPoint(4, 6);
        addInitialPoint(4, 9);

        addInitialPoint(5, 6);
        addInitialPoint(5, 9);

        addInitialPoint(6, 3);
        addInitialPoint(6, 4);
        addInitialPoint(6, 5);
        addInitialPoint(6, 6);
        addInitialPoint(6, 9);
        addInitialPoint(6, 10);
        addInitialPoint(6, 11);
        addInitialPoint(6, 12);

        addInitialPoint(7, 3);
        addInitialPoint(7, 12);

        addInitialPoint(8, 3);
        addInitialPoint(8, 12);

        addInitialPoint(9, 3);
        addInitialPoint(9, 4);
        addInitialPoint(9, 5);
        addInitialPoint(9, 6);
        addInitialPoint(9, 9);
        addInitialPoint(9, 10);
        addInitialPoint(9, 11);
        addInitialPoint(9, 12);

        addInitialPoint(10, 6);
        addInitialPoint(10, 9);

        addInitialPoint(11, 6);
        addInitialPoint(11, 9);

        addInitialPoint(12, 6);
        addInitialPoint(12, 7);
        addInitialPoint(12, 8);
        addInitialPoint(12, 9);
    }

    public void addInitialPoint(int x, int y) {
        Point pointToAdd = new Point(x, y);
        initialPoints.add(pointToAdd);

        if (!pointsPlaced.contains(pointToAdd))
            pointsPlaced.add(pointToAdd);
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
     */
    public void askPoint() {
        Scanner scannerPoint = new Scanner(System.in);
        int x = 0;
        int y = 0;
        boolean isXValid = false;
        boolean isYValid = false;

        do {
            // Ask for the x-value.
            System.out.println("What is the x coordinate of the point to place?");
            try {
                // Try if the x-value is valid.
                x = scannerPoint.nextInt();
                new Point(x, 1);
                isXValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("The x value is not valid.");
                scannerPoint.nextLine();
            }
        } while (!isXValid);

        do {
            // Ask for the y-value.
            System.out.println("What is the y coordinate of the point to place?");
            try {
                // Try if the x-value is valid.
                y = scannerPoint.nextInt();
                new Point(1, y);
                isYValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("The y value is not valid.");
                scannerPoint.nextLine();
            }
        } while (!isYValid);

        // Here, the point entered is valid, so it is created.
        Point pointToAdd = new Point(x, y);

        // Check if the point already exists.
        if (pointsPlaced.contains(pointToAdd)) {
            System.out.println("The point already exists.");
            askPoint();
        // Check if the point can be placed.
        } else if (!canPointBePlayed(pointToAdd)) {
            System.out.println("The point cannot be placed here.");
            askPoint();
        // Play the point.
        } else
            playPoint(x,y);
    }

    /**
     * Check if the point can be placed here.
     * @param pointToPlay the point to be placed.
     * @return True if the point can be placed, false otherwise.
     */
    protected abstract boolean canPointBePlayed(Point pointToPlay);

    /**
     * Check if there is a potential alignment of 5 points in the specified direction starting from the given coordinates.
     * @param x The x-coordinate to start checking from.
     * @param y The y-coordinate to start checking from.
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
     * @param x The x-coordinate of the point to be added.
     * @param y The y-coordinate of the point to be added.
     */
    protected abstract void playPoint(int x, int y);

    /**
     * Adds a new line to the list of lines on the game grid based on the given points and direction.
     * @param pointsOfNewLine The list of points forming the new line.
     * @param directionOfNewLine The direction of the new line (HORIZONTAL, VERTICAL, B_DIAGONAL, T_DIAGONAL).
     */
    protected void addLine(ArrayList<Point> pointsOfNewLine, Direction directionOfNewLine) {
        Line newLine = new Line(pointsOfNewLine, directionOfNewLine, lines.size() + 1);
        lines.add(newLine);
    }

    public ArrayList<Point> getPointsPlaced() {
        return pointsPlaced;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }


}
