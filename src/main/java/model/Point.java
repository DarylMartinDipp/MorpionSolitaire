package model;

/**
 * A point in a board.
 */
public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Compares this point object with another object for equality.
     * @param pointToCompare The object to compare with this point.
     * @return True if the objects are equal, false otherwise.
     */
    public boolean equals(Object pointToCompare) {
        if (this == pointToCompare)
            return true;
        if (pointToCompare == null || getClass() != pointToCompare.getClass())
            return false;
        return x == ((Point) pointToCompare).x && y == ((Point) pointToCompare).y;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
