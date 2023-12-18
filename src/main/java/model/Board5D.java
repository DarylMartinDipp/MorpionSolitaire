package model;

/**
 * The game board in the 5D mode.
 */
public class Board5D extends Board{
    /**
     * Checks if the point can be placed here.
     * @param pointToPlay the point to be placed.
     * @param hasToPlay True if the points hs to be played, false otherwise.
     *                  False is for having all the playable points.
     * @return True if the point can be placed, false otherwise.
     */
    @Override
    public boolean canPointBePlayed(Point pointToPlay, boolean hasToPlay) {
        int x = pointToPlay.getX();
        int y = pointToPlay.getY();

        // Check for all directions if the point can be placed.
        for (Direction direction : Direction.values()) {
            if (hasAlignmentInDirection(x, y, direction, hasToPlay))
                return true;
        }

        return false;
    }

    /**
     * Plays the point as in the mode.
     * @param x The x-coordinate of the point to be added.
     * @param y The y-coordinate of the point to be added.
     */
    @Override
    protected void playPoint(int x, int y) {
        addPoint(x,y);
        System.out.println("Point successfully added.");
    }

    /**
     * This method is useless in Board5D.
     * @param userPointChoice The chosen point to play on the game board.
     */
    @Override
    public void process5TUserChoice(Point userPointChoice) {
    }
}
