public class GameManager {
    private Player player;
    private Board board;


    public GameManager(Player player) {
        this.player = player;
    }

    public void initBoard(){
        this.board = new Board();
    }

    public void StartGame(){

    }

    public boolean isGameFinished() {
        return false;
    }

    public static void main(String[] args) {
        GameManager gm = new GameManager(new Player("Daryl"));
        gm.initBoard();
    }
}
