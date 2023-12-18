package controller;

import model.Board;
import model.Board5D;
import model.Board5T;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testCreateBoard5T() {
        GameManager gameManager = new GameManager("5T");
        Board board = gameManager.getBoard();
        assertTrue(board instanceof Board5T, "Expected Board5T for game mode 5T");
    }

    @Test
    void testCreateBoard5D() {
        GameManager gameManager = new GameManager("5D");
        Board board = gameManager.getBoard();
        assertTrue(board instanceof Board5D, "Expected Board5D for game mode 5D");
    }

    @Test
    void testInvalidMode() {
        GameManager gameManager = new GameManager("InvalidMode");
        Board board = gameManager.getBoard();

        assertNull(board, "Board should be null for an invalid mode");

        String expectedErrorMessage = "Invalid mode. Please choose 5T or 5D.";
        assertTrue(outContent.toString().contains(expectedErrorMessage),
                "Expected error message not found in the output");
    }



}
