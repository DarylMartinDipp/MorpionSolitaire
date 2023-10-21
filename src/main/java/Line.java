import java.util.ArrayList;

public class Line {
    private final ArrayList<Point> points;
    private final String direction;

    public Line(ArrayList<Point> points, String direction) {
        this.points = points;
        this.direction = direction;
    }

    public ArrayList<Point> getPointsOfTheLine() {
        return points;
    }

    public String getDirection() {
        return direction;
    }

    public ArrayList<Point> extremities() {
        ArrayList<Point> extremities = new ArrayList<Point>();
        extremities.add(this.points.get(0));
        extremities.add(this.points.get(this.points.size() - 1));
        return extremities;
    }
}
