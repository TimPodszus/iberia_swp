package de.uol.swp.server.game.store;

import de.uol.swp.common.user.User;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStoreTest {

    IGameStore gameStore;
    User mockUser;
    List<User> users;
    void setGameStore() {
        this.gameStore = GameStore.getInstance();
        this.mockUser = mock(User.class);
        users = new ArrayList<>();
        users.add(mockUser);
    }


    @Test
    void testGameStore() {
        setGameStore();
        IGame game = new Game(users, 3);

        this.gameStore.addGame("test", game);

        assertEquals(game, this.gameStore.getGame("test"));
    }
}
