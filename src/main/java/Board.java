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
                if (pointsPlaced.contains(new Point(j,i))) // Inversé pour avoir x en horizontal et j en vertical
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
    // Beaucoup de classes dans ce genre
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
        } else if (!canPointBePlayed(pointToAdd)) {
            System.out.println("The point cannot be placed here.");
            askPoint();
        } else {
            addPoint(x,y);
            System.out.println("Point successfully added.");
        }
    }

    private boolean canPointBePlayed(Point pointToPlay) {
        int x = pointToPlay.getX();
        int y = pointToPlay.getY();

        for (Direction direction : Direction.values()) {
            if (hasAlignmentInDirection(x, y, direction)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAlignmentInDirection(int x, int y, Direction direction) {
        int dx = 0, dy = 0;

        switch (direction) {
            case HORIZONTAL -> dx = 1;
            case VERTICAL -> dy = 1;
            case L_DIAGONAL -> {
                dx = -1;
                dy = 1;
            }
            case R_DIAGONAL -> {
                dx = 1;
                dy = 1;
            }
        }

        int count = 0;
        for (int i = -4; i <= 4; i++) {
            if (i == 0) {
                continue; // Ignorer le point central
            }
            int currentX = x + i * dx;
            int currentY = y + i * dy;

            if (currentX < 0 || currentX >= GameManager.DIMENSION || currentY < 0 || currentY >= GameManager.DIMENSION) {
                continue;
            }

            Point point = new Point(currentX, currentY);
            if (pointsPlaced.contains(point)) {
                count++; // Incrémenter le compteur si un point est trouvé
            } else {
                continue;
            }

            if (count == 4) {
                return true; // 4 points alignés ont été trouvés, le 5ème compléterait la ligne
            }
        }

        return false; // Aucun alignement de 4 points dans cette direction
    }
}
