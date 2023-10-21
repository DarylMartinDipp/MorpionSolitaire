public class Point {
    private final int x;
    private final int y;
    private boolean used;

    public Point(int x, int y) {
        if (!valid(x, y))
            throw new IllegalArgumentException("The coordinates are not valid.");
        this.x = x;
        this.y = y;
        this.used = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean valid(int x, int y) {
        return ((x >= 0 && x < 16) && (y >= 0 && y < 16));
    }

    public void usePoint() {
        if (used)
            throw new IllegalStateException("This point is already used.");
        this.used = true;
    }

    public boolean equals(Point pointToCompare) {
        return (this.x == pointToCompare.x) && (this.y == pointToCompare.y);
    }

    @Override
    public String toString() {
        String stringPoint = "The Point in (" + x + ";" + y + ") is";
        if (used)
            stringPoint += " used.";
        else
            stringPoint += " not used.";
        return stringPoint;
    }
}
