package model;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest extends ApplicationTest {

    @Test
    public void testInitArrow() {
        Board testBoard = new Board5D();

        int pointsWhenInitialised = testBoard.getPointsPlaced().size();

        testBoard.initArrow();
        assertEquals(pointsWhenInitialised, testBoard.getPointsPlaced().size());
    }

    @Test
    public void testAddPoint() {
        Board testBoard = new Board5D();

        int pointsWhenInitialised = testBoard.getPointsPlaced().size();

        testBoard.addPoint(13, 7);
        assertTrue(testBoard.getPointsPlaced().contains(new Point(13, 7)));

        testBoard.addPoint(13, 7);
        assertEquals(1 + pointsWhenInitialised, testBoard.getPointsPlaced().size());
    }

    @Test
    public void testAskPoint() {
        Board testBoard = new Board5D();

        int pointsWhenInitialised = testBoard.getPointsPlaced().size();

        testBoard.askPoint(13, 7);
        assertTrue(testBoard.getPointsPlaced().contains(new Point(13, 7)));
        assertEquals(1, testBoard.getScore());
        assertEquals(1, testBoard.getPointsAddedByUser().size());

        testBoard.askPoint(13, 7);
        assertEquals(1 + pointsWhenInitialised, testBoard.getPointsPlaced().size());
        assertEquals(1, testBoard.getScore());
        assertEquals(1, testBoard.getPointsAddedByUser().size());

        testBoard.askPoint(0, 0);
        assertEquals(1 + pointsWhenInitialised, testBoard.getPointsPlaced().size());
        assertEquals(1, testBoard.getScore());
        assertEquals(1, testBoard.getPointsAddedByUser().size());
    }

    @Test
    public void testCanPointBePlayed() {
        Board testBoard = new Board5D();

        assertTrue(testBoard.canPointBePlayed(new Point(13, 7), true));
        assertFalse(testBoard.canPointBePlayed(new Point(13, 7), false));

        testBoard.askPoint(13, 7);

        assertFalse(testBoard.canPointBePlayed(new Point(13, 7), true));
        assertFalse(testBoard.canPointBePlayed(new Point(13, 7), false));
    }

    @Test
    public void testHasAlignmentInDirection() {
        Board testBoard = new Board5D();

        assertTrue(testBoard.hasAlignmentInDirection(13, 7, Direction.HORIZONTAL, true));
        assertEquals(1, testBoard.getLines().size());

        assertFalse(testBoard.hasAlignmentInDirection(13, 7, Direction.HORIZONTAL, true));
        assertEquals(1, testBoard.getLines().size());

        assertTrue(testBoard.hasAlignmentInDirection(4, 3, Direction.VERTICAL, true));
        assertEquals(2, testBoard.getLines().size());
    }

    @Test
    public void testGetOffset() {
        Board testBoard = new Board5D();

        assertArrayEquals(new int[]{1, 0}, testBoard.getOffset(Direction.HORIZONTAL));
        assertArrayEquals(new int[]{0, 1}, testBoard.getOffset(Direction.VERTICAL));
        assertArrayEquals(new int[]{-1, 1}, testBoard.getOffset(Direction.B_DIAGONAL));
        assertArrayEquals(new int[]{1, 1}, testBoard.getOffset(Direction.T_DIAGONAL));
    }

    @Test
    public void testIsPointPartOfLine() {
        Board testBoard = new Board5D();

        testBoard.askPoint(1, 2);
        assertFalse(testBoard.isPointPartOfLine(new Point(1, 2), Direction.HORIZONTAL));
        assertFalse(testBoard.isPointPartOfLine(new Point(1, 2), Direction.VERTICAL));

        assertTrue(testBoard.hasAlignmentInDirection(13, 7, Direction.HORIZONTAL, true));

        assertTrue(testBoard.isPointPartOfLine(new Point(13, 7), Direction.HORIZONTAL));
        assertFalse(testBoard.isPointPartOfLine(new Point(13, 7), Direction.VERTICAL));
    }

    @Test
    public void testPlayPoint() {
        Board testBoard = new Board5D();

        testBoard.playPoint(1, 2);
        assertTrue(testBoard.getPointsPlaced().contains(new Point(1, 2)));
    }

    @Test
    public void testAddLine() {
        Board testBoard = new Board5D();

        testBoard.addLine(new ArrayList<>(), Direction.HORIZONTAL);
        assertEquals(1, testBoard.getLines().size());
    }
}
