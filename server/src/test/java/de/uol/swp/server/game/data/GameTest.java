package de.uol.swp.server.game.data;

import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.EpidemicCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.GameStateChangeListener;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    private IGame game;

    @BeforeEach
    void setUp() {
        game = new Game(3, "123");
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getInfectionCardDrawPile());
        assertEquals(1, game.getInfectionCounter());
        assertEquals(0, game.getEscalationStage());
        assertEquals(14, game.getWaterTreatmentsLeft());
        assertEquals(20, game.getTracksLeft());
        assertFalse(game.getPlayerCardDrawPile()
                        .isEmpty());
        assertNotNull(game.getState());
    }

    @Test
    void testCreateInfectionCards() {
        assertEquals(
                48,
                game.getInfectionCardDrawPile()
                    .size()
        );
    }

    @Test
    void testCreatePlayerCards() {
        assertEquals(
                55,
                game.getPlayerCardDrawPile()
                    .size()
        );
        assertTrue(game.getPlayerCardDrawPile()
                       .get(0) instanceof CityCard || game.getPlayerCardDrawPile()
                                                          .get(0) instanceof EpidemicCard || game.getPlayerCardDrawPile()
                                                                                                 .get(0) instanceof EventCard);
    }

    @Test
    void testGameStartShuffle() {
        assertEquals(
                55,
                game.getPlayerCardDrawPile()
                    .size()
        );
    }

    @Test
    void testGetCurrentPlayer() {
        IUser user = new User("testUser", "testPassword");
        game.getPlayers()
            .add(new Player(user));
        assertNotNull(game.getCurrentPlayer());
    }

    @Test
    void testSplitIntoSubDecks() {
        ICity city = mock(ICity.class);
        List<ICard> deck = new ArrayList<>();
        deck.add(new CityCard(1, "City1", city));
        List<List<ICard>> subDecks = game.splitIntoSubDecks(deck, 1);
        assertEquals(1, subDecks.size());
    }

    @Test
    void testSetState() {
        StartState startState = new StartState();
        game.setState(startState);
        assertEquals(startState, game.getState());
    }

    @Test
    void testGetPlayer() {
        IPlayer player = mock(IPlayer.class);
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(new User("testUser", "testPassword"));
        assertEquals(player, game.getPlayer("testUser"));
        assertNull(game.getPlayer("nonExistentUser"));
    }

    @Test
    void testSetStateWithListener() {
        GameStateChangeListener listener = mock(GameStateChangeListener.class);
        game.setGameStateChangeListener(listener);
        StartState startState = new StartState();
        game.setState(startState);
        verify(listener).onGameStateChange(game);
    }

    @Test
    void testGameStartShuffleWithInvalidNumSubDecks() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> game.gameStartShuffle(0));
        assertEquals("Number of sub-decks must be greater than zero.", exception.getMessage());
    }
}

