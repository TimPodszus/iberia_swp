package de.uol.swp.server.game.store;

import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameStoreTest {

    private GameStore gameStore;
    private IGame game;

    @BeforeEach
    void setUp() {
        gameStore = GameStore.getInstance();
        game = mock(IGame.class);
    }


    @Test
    void testAddAndGetGame() {
        String lobbyId = "lobby123";
        gameStore.addGame(lobbyId, game);
        IGame retrievedGame = gameStore.getGame(lobbyId);
        assertSame(game, retrievedGame, "The retrieved game should be the same as the one added.");
    }
}

