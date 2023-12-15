import java.util.Scanner;

/**
 * The game board in the 5T mode.
 */
public class Board5T extends Board {
    protected Direction dir;

    /**
     * Check if the point can be placed here.
     * @param pointToPlay the point to be placed.
     * @return True if the point can be placed, false otherwise.
     */
    @Override
    protected boolean canPointBePlayed(Point pointToPlay) {
        int x = pointToPlay.getX();
        int y = pointToPlay.getY();

        for (Direction direction : Direction.values()) {
            if (hasAlignmentInDirection(x, y, direction)) {
                dir = direction;
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
    @Override
    protected void playPoint(int x, int y) {
        Scanner scannerPlay = new Scanner(System.in);
        String playerChoiceInput;

        if (!isAdjacentToAlignment(x, y, dir)) {
            addPoint(x, y);
            System.out.println("Point successfully added.");
        }
        else {
            do {
                System.out.println("Choose how to play the point (T or D): ");
                playerChoiceInput = scannerPlay.nextLine().toUpperCase();

                switch (playerChoiceInput) {
                    case "D" -> {
                        addPoint(x, y);
                        System.out.println("Point successfully added in D mode.");
                    }
                    case "T" -> {
                        addPoint(x, y);
                        System.out.println("Point successfully added in T mode.");
                    }
                    default -> System.out.println("Invalid mode for the point. Please choose 5T or 5D.");
                }
            } while (!playerChoiceInput.equals("T") && !playerChoiceInput.equals("D"));
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
        int count = 1; // Start at 1 since the current point is already included.

        // Check points above and below the current position.
        for (int i = 1; i <= 4; i++) {
            int currentXUp = x - i * dx;
            int currentYUp = y - i * dy;

            int currentXDown = x + i * dx;
            int currentYDown = y + i * dy;

            // Check valid coordinates for the point above.
            if (isValidCoordinate(currentXUp, currentYUp)) {
                Point upPoint = new Point(currentXUp, currentYUp);

                // Check if the point above is placed.
                if (pointsPlaced.contains(upPoint))
                    count++;
                else
                    break;
            }

            // Check valid coordinates for the point below
            if (isValidCoordinate(currentXDown, currentYDown)) {
                Point downPoint = new Point(currentXDown, currentYDown);

                // Check if the point below is placed
                if (pointsPlaced.contains(downPoint))
                    count++;
                else
                    break;
            }
        }

        return count >= 5 && dx != dy;
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
}
