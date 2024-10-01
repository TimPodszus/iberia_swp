package de.uol.swp.server.game.store;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStoreTest {

    IGameStore gameStore;

    void setGameStore() {
        this.gameStore = GameStore.getInstance();
    }

    @Test
    void testGameStore() {
        setGameStore();
        IGame game = new Game();

        this.gameStore.addGame("test", game);

        assertEquals(game, this.gameStore.getGame("test"));
    }
}
