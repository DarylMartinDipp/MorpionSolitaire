package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class LineTest {

    @Test
    public void testGetPointsOfTheLine() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(2, 3));
        points.add(new Point(3, 4));
        points.add(new Point(4, 5));
        points.add(new Point(5, 6));

        Line line = new Line(points, Direction.HORIZONTAL);
        assertEquals(points, line.getPointsOfTheLine());
    }

    @Test
    public void testGetDirection() {
        ArrayList<Point> points = new ArrayList<>();
        Line line = new Line(points, Direction.VERTICAL);
        assertEquals(Direction.VERTICAL, line.getDirection());

        line = new Line(points, Direction.B_DIAGONAL);
        assertEquals(Direction.B_DIAGONAL, line.getDirection());
    }
}
