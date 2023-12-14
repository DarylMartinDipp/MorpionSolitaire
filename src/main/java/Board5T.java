import java.util.ArrayList;
import java.util.Scanner;

public class Board5T extends Board {

    @Override
    protected boolean canPointBePlayed(Point pointToPlay) {
        int x = pointToPlay.getX();
        int y = pointToPlay.getY();

        for (Direction direction : Direction.values()) {
            if (hasAlignmentInDirection(x, y, direction) || canExtendExistingLine(x, y, direction)) {
                return true;
            }
        }
        return false;
    }

    protected boolean canExtendExistingLine(int x, int y, Direction direction) {
        int dx = 0, dy = 0;

        switch (direction) {
            case HORIZONTAL -> dx = 1;
            case VERTICAL -> dy = 1;
            case B_DIAGONAL -> {
                dx = -1;
                dy = 1;
            }
            case T_DIAGONAL -> {
                dx = 1;
                dy = 1;
            }
        }

        // Chercher toutes les lignes existantes dans la direction donnée
        ArrayList<Line> existingLinesInDirection = new ArrayList<>();
        for (Line existingLine : lines) {
            if (existingLine.getDirection() == direction) {
                existingLinesInDirection.add(existingLine);
            }
        }

        // Calculer les coordonnées du dernier point existant
        int lastX = x - 4 * dx;
        int lastY = y - 4 * dy;

        // Vérifier si le dernier point existant fait partie d'une ligne existante
        for (Line existingLine : existingLinesInDirection) {
            if (existingLine.getPointsOfTheLine().contains(new Point(lastX, lastY))) {
                // Demander à l'utilisateur à partir de quel point il veut étendre
                System.out.println("Choose from which point to extend:");
                int extensionPointIndex = getUserInputForExtension(existingLine);
                // Ajouter la nouvelle ligne étendue à partir du point choisi
                addExtendedLine(existingLine, direction, extensionPointIndex, x, y);
                return true;
            }
        }

        // Aucune ligne existante trouvée
        return false;
    }

    private int getUserInputForExtension(Line existingLine) {
        Scanner scanner = new Scanner(System.in);
        existingLine.displayLine(); // Afficher la ligne existante complète
        System.out.println("Enter the index of the point from which to extend (0-4): ");
        int extensionPointIndex;
        do {
            extensionPointIndex = scanner.nextInt();
        } while (extensionPointIndex < 0 || extensionPointIndex > 4); // S'assurer que l'indice est valide
        return extensionPointIndex;
    }


    private void addExtendedLine(Line existingLine, Direction direction, int extensionPointIndex, int newX, int newY) {
        ArrayList<Point> pointsOfNewLine = new ArrayList<>(existingLine.getPointsOfTheLine());
        pointsOfNewLine.add(extensionPointIndex, new Point(newX, newY));
        addLine(pointsOfNewLine, direction);
    }



    // Méthode pour vérifier si deux points sont adjacents
    private boolean isAdjacent(Point p1, Point p2) {
        int dx = Math.abs(p1.getX() - p2.getX());
        int dy = Math.abs(p1.getY() - p2.getY());

        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);
    }

    @Override
    protected void addLine(ArrayList<Point> pointsOfNewLine, Direction directionOfNewLine) {
        Line newLine = new Line(pointsOfNewLine, directionOfNewLine, 1);

        // Vérifier si la nouvelle ligne peut toucher une ligne existante
        for (Line existingLine : lines) {
            if (canLinesTouch(newLine, existingLine)) {
                lines.add(newLine);
                score++;
                return;
            }
        }

        // Si la nouvelle ligne ne touche aucune ligne existante, ajouter la ligne normalement
        lines.add(newLine);
        score++;
    }

    // Ajouter la méthode canLinesTouch pour le jeu 5T
    private boolean canLinesTouch(Line line1, Line line2) {
        if (line1.getDirection() == line2.getDirection()) {
            ArrayList<Point> points1 = line1.getPointsOfTheLine();
            ArrayList<Point> points2 = line2.getPointsOfTheLine();

            // Vérifier si les lignes se touchent aux extrémités
            return points1.get(0).equals(points2.get(points2.size() - 1)) || points2.get(0).equals(points1.get(points1.size() - 1));
        }
        return false;
    }

}
