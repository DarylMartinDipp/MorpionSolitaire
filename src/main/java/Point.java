public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        if (!valid(x, y))
            throw new IllegalArgumentException("The coordinates are not valid.");
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean valid(int x, int y) {
        return ((x >= 0 && x < 16) && (y >= 0 && y < 16));
    }

    public boolean equals(Object pointToCompare) {
        if (this == pointToCompare) {
            return true;
        }
        if (pointToCompare == null || getClass() != pointToCompare.getClass()) {
            return false;
        }
        return x == ((Point) pointToCompare).x && y == ((Point) pointToCompare).y;

    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
