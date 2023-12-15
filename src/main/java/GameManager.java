import java.util.Scanner;

public class GameManager {
    private final Player player;
    private static Board board;
    public static int DIMENSION = 16;


    public GameManager(Player player) {
        this.player = player;
    }

    public void initBoard() {
        Scanner scanner = new Scanner(System.in);

        String mode;
        do {
            System.out.println("Choose the game mode (5T or 5D): ");
            mode = scanner.nextLine().toUpperCase();

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



    public static void main(String[] args) {
        GameManager gm = new GameManager(new Player("Daryl"));
        gm.initBoard();

        int maxPoint = 10;
        while (maxPoint > 0) {
            board.askPoint();
            board.displayBoard();
            maxPoint--;
        }
    }
}
