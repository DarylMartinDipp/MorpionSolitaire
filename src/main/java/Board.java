import java.util.ArrayList;
import java.util.Scanner;

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
        displayBoard();
    }

    public void displayBoard() {
        for (int i = 0; i < GameManager.DIMENSION; i++) {
            for (int j = 0; j < GameManager.DIMENSION; j++) {
                if (pointsPlaced.contains(new Point(i,j)))
                    System.out.print("O ");
                else
                    System.out.print(". ");
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

    // Private car on doit utiliser askPoint ou initBoard pour l'utiliser uniquement
    private void addPoint(int x, int y) {
        Point pointToAdd = new Point(x, y);
        if (!pointsPlaced.contains(pointToAdd))
            pointsPlaced.add(pointToAdd);
    }

    public void askPoint() {
        Scanner scannerPoint = new Scanner(System.in);
        int x = 0;
        int y = 0;
        boolean isXValid = false;
        boolean isYValid = false;

        do {
            System.out.println("What is the x coordinate of the point to place?");
            try {
                x = scannerPoint.nextInt();
                new Point(x, 1);
                isXValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("The x value is not valid.");
                scannerPoint.nextLine();
            }
        } while (!isXValid);

        do {
            System.out.println("What is the y coordinate of the point to place?");
            try {
                y = scannerPoint.nextInt();
                new Point(1, y);
                isYValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("The y value is not valid.");
                scannerPoint.nextLine();
            }
        } while (!isYValid);

        Point pointToAdd = new Point(x, y);

        if (pointsPlaced.contains(pointToAdd)) {
            System.out.println("The point already exists.");
            askPoint();
        } else {
            addPoint(x,y);
            System.out.println("Point successfully added.");
        }

    }
}
