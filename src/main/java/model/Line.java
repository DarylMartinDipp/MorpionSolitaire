package model;

import java.util.ArrayList;

/**
 * A line made up of 5 points.
 */
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
}
