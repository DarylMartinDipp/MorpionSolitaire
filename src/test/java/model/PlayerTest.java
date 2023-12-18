package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testGetName() {
        String playerName = "John";
        Player player = new Player(playerName);
        assertEquals(playerName, player.getName());
    }
}
