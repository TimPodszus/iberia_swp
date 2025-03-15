package de.uol.swp.server.game.store;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStoreTest {

    private GameStore gameStore;

    @BeforeEach
    void setUp() {
        gameStore = GameStore.getInstance();
    }


    @Test
    void testAddAndGetGame() {
        String lobbyId = "gameStoreTest";
        IGame game = new Game(2, lobbyId);
        gameStore.addGame(lobbyId, game);
        IGame retrievedGame = gameStore.getGame(lobbyId);
        assertEquals(game, retrievedGame, "The retrieved game should be the same as the one added.");
    }
}

