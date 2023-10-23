import java.util.ArrayList;

public class Board {
    private ArrayList<Point> pointsPlaced;
    private int score;

    public Board() {
        this.pointsPlaced = new ArrayList<>();
        this.score = 0;
        initBoard();
    }

    public void initBoard() {
        initCross();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (pointsPlaced.contains(new Point(i,j)))
                    System.out.print("O");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
    }

    public void initCross() {
        addPoint(3, 6);
        addPoint(3, 7);
        addPoint(3, 8);
        addPoint(3, 9);

        addPoint(4, 6);
        addPoint(4, 9);

        addPoint(5, 6);
        addPoint(5, 9);

        addPoint(6, 3);
        addPoint(6, 4);
        addPoint(6, 5);
        addPoint(6, 6);
        addPoint(6, 9);
        addPoint(6, 10);
        addPoint(6, 11);
        addPoint(6, 12);

        addPoint(7, 3);
        addPoint(7, 12);

        addPoint(8, 3);
        addPoint(8, 12);

        addPoint(9, 3);
        addPoint(9, 4);
        addPoint(9, 5);
        addPoint(9, 6);
        addPoint(9, 9);
        addPoint(9, 10);
        addPoint(9, 11);
        addPoint(9, 12);

        addPoint(10, 6);
        addPoint(10, 9);

        addPoint(11, 6);
        addPoint(11, 9);

        addPoint(12, 6);
        addPoint(12, 7);
        addPoint(12, 8);
        addPoint(12, 9);
    }

    // TODO : verify that we can place the point
    public void addPoint(int x, int y) {
        Point pointToAdd = new Point(x, y);
        if (!pointsPlaced.contains(pointToAdd))
            pointsPlaced.add(pointToAdd);
    }
}
