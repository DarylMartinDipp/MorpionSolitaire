import java.util.ArrayList;

public class Line {
    private final ArrayList<Point> points;

    public Line(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Point> getPointsOfTheLine() {
        return points;
    }
}
