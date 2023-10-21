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

    public boolean equals(Point pointToCompare) {
        if (pointToCompare == null)
            return false;
        if (getClass() != pointToCompare.getClass())
            return false;
        return (this.x == pointToCompare.x) && (this.y == pointToCompare.y);
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
