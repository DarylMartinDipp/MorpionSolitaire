package controller;

import model.Board;
import model.Board5D;
import model.Board5T;

public class GameManager {
    private static Board board;
    public static int DIMENSION = 16;

    /**
     * Constructs a board with a specified game mode.
     * @param mode The game mode to set (5T or 5D).
     */
    public GameManager(String mode) {
        do {
            switch (mode) {
                case "5T" -> board = new Board5T();
                case "5D" -> board = new Board5D();
                default -> System.out.println("Invalid mode. Please choose 5T or 5D.");
            }
        } while (!mode.equals("5T") && !mode.equals("5D"));
    }

    public static Board getBoard() {
        return board;
    }
}
