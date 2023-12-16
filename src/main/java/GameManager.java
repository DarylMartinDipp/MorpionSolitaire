import java.util.Scanner;

public class GameManager {
    private static String mode;

    static Board board;
    public static double DIMENSION = 16;

    public int length;


    public GameManager() {

    }






    public void initBoard(String mode) {


        do {

            switch (mode) {
                case "5T":
                    if (!(board instanceof Board5T)) {
                        board = new Board5T();
                    }
                    break;
                case "5D":
                    if (!(board instanceof Board5D)) {
                        board = new Board5D();
                    }
                    break;
                default:
                    System.out.println("Invalid mode. Please choose 5T or 5D.");
            }
        } while (!mode.equals("5T") && !mode.equals("5D"));
    }

    public static Board getBoard() {
        return board;
    }

    public static void main(String[] args) {
        GameManager gm = new GameManager();
        gm.initBoard(mode);

        int maxPoint = 10;
        while (maxPoint > 0) {
            board.askPoint();
            board.displayBoard();
            maxPoint--;
        }
    }


    public double getDimension() {
        return DIMENSION;
    }

}
