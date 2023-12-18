package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    public void testGetX() {
        int x = 3;
        int y = 5;
        Point point = new Point(x, y);
        assertEquals(x, point.getX());
    }

    @Test
    public void testGetY() {
        int x = 3;
        int y = 5;
        Point point = new Point(x, y);
        assertEquals(y, point.getY());
    }

    @Test
    public void testEquals() {
        Point point1 = new Point(3, 5);
        Point point2 = new Point(3, 5);
        Point point3 = new Point(1, 2);

        assertEquals(point1, point2);
        assertNotEquals(point1, point3);
    }

    @Test
    public void testToString() {
        Point point = new Point(3, 5);
        assertEquals("(3;5)", point.toString());
    }
}
