package de.uol.swp.server.cards.management;

import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for CardManagement.
 */
public class CardManagementTest {

    private static final String LOBBY_ID = "1";

    @Mock
    IGame game;

    private CardManagement cardManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(game.getGameId()).thenReturn(LOBBY_ID);
        GameStore.getInstance()
                 .addGame(LOBBY_ID, game);
        cardManagement = new CardManagement();
    }

    /**
     * Tests the playCard method for a regular card.
     */
    @Test
    void testPlayCard() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);

        ICard card = mock(CityCard.class);
        when(card.getId()).thenReturn(1);
        player.setCards(new ArrayList<>(List.of(card)));

        cardManagement.playCard("1", "user", 1);

        assertEquals(
                0,
                player.getCards()
                      .size()
        );
    }

    /**
     * Tests the playCard method for an event card.
     */
    @Test
    void testPlayEventCard() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);

        EventCard card = mock(EventCard.class);
        when(card.getId()).thenReturn(1);
        player.setCards(new ArrayList<>(List.of(card)));

        cardManagement.playCard("1", "user", 1);

        assertEquals(
                0,
                player.getCards()
                      .size()
        );
        verify(card).execute("1", "user");
        verify(game).setState(any(EventState.class));
    }
}
