public class Board {

    public Board() {
        initBoard();
    }

    public void initBoard() {
        for (int line = 0; line < 16; line++) {
            for (int column = 0; column < 16; column++)
                new Point(line, column);
        }


    }
}
