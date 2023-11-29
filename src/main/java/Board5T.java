import java.util.ArrayList;
import java.util.Scanner;


public class Board5T extends Board {
        private boolean isAdjacentToAlignment(int x, int y, Direction direction) {
            // Déterminer le décalage (dx, dy) en fonction de la direction
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

            // Vérifier l'alignement adjacent
            if (checkAdjacentAlignment(x, y, dx, dy)) {
                return true;
            }

            return false;
        }

    private boolean checkAdjacentAlignment(int x, int y, int dx, int dy) {
        int count = 1; // Commencer à 1 car le point actuel est déjà inclus

        // Vérifier les points au-dessus et en dessous de la position actuelle
        for (int i = 1; i <= 4; i++) {
            int currentXUp = x - i * dx;
            int currentYUp = y - i * dy;

            int currentXDown = x + i * dx;
            int currentYDown = y + i * dy;

            // Vérifier les coordonnées valides pour le point au-dessus
            if (currentXUp >= 0 && currentXUp < GameManager.DIMENSION && currentYUp >= 0 && currentYUp < GameManager.DIMENSION) {
                Point upPoint = new Point(currentXUp, currentYUp);

                // Vérifier si le point au-dessus est placé
                if (pointsPlaced.contains(upPoint)) {
                    count++;
                } else {
                    break; // S'arrêter si on rencontre un espace
                }
            }

            // Vérifier les coordonnées valides pour le point en dessous
            if (currentXDown >= 0 && currentXDown < GameManager.DIMENSION && currentYDown >= 0 && currentYDown < GameManager.DIMENSION) {
                Point downPoint = new Point(currentXDown, currentYDown);

                // Vérifier si le point en dessous est placé
                if (pointsPlaced.contains(downPoint)) {
                    count++;
                } else {
                    break; // S'arrêter si on rencontre un espace
                }
            }
        }

        // Exclure les alignements adjacents en diagonale avec un décalage constant
        return count >= 5 && (dx != dy) && (dx != 0 || dy != 0);
    }










    @Override
    public void askPoint() {
        Scanner scannerPoint = new Scanner(System.in);
        int x = 0;
        int y = 0;
        boolean isXValid = false;
        boolean isYValid = false;
        String mode;

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
                if (!isAdjacentToAlignment(x, y, dir)) {
                    addPoint(x, y);
                    System.out.println("Point successfully added.");
                }
                else {
                    scannerPoint.nextLine();
                    do {
                    System.out.println("Choose how to play the point (T or D): ");
                    mode = scannerPoint.nextLine().toUpperCase();

                    switch (mode) {
                        case "D":
                            addPoint(x, y);
                            System.out.println("Point successfully added in D mode.");
                            break;
                        case "T":
                            addPoint(x, y);
                            System.out.println("Point successfully added in T mode.");
                            break;
                        default:
                            System.out.println("Invalid mode for the point. Please choose 5T or 5D.");
                    }
                } while (!mode.equals("T") && !mode.equals("D"));
                }
            }
        }
}