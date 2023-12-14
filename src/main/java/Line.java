import java.util.ArrayList;

public class Line {
    private final ArrayList<Point> points;
    private final Direction direction;

    public Line(ArrayList<Point> points, Direction direction) {
        this.points = points;
        this.direction = direction;
    }

    public ArrayList<Point> getPointsOfTheLine() {
        return points;
    }

    public Direction getDirection() {
        return direction;
    }

    public void displayLine() {
        Point[] pointsOfTheLine = new Point[0];
        for (Point point : pointsOfTheLine) {
            System.out.print("(" + point.getX() + ", " + point.getY() + ") ");
        }
        System.out.println();
    }
}
