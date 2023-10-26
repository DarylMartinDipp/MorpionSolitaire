public class GameManager {
    private Player player;
    private static Board board;
    public static int DIMENSION = 16;


    public GameManager(Player player) {
        this.player = player;
    }

    public void initBoard(){
        board = new Board();
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
