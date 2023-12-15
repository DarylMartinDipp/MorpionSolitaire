import java.util.ArrayList;

/**
 * A line made up of 5 points.
 */
public class Line {
    private final ArrayList<Point> points;
    private final Direction direction;
    private final int lineNumber;

    public Line(ArrayList<Point> points, Direction direction, int lineNumber) {
        this.points = points;
        this.direction = direction;
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public ArrayList<Point> getPointsOfTheLine() {
        return points;
    }

    public Direction getDirection() {
        return direction;
    }
}
