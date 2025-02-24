package de.uol.swp.server.cards.management;

import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.cards.data.eventcards.TreatWaterEventCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.InfectionState;
import de.uol.swp.server.game.states.PlayerTurnState;
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

import static org.junit.jupiter.api.Assertions.*;
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
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));

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
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));

        cardManagement.playCard("1", "user", 1);

        assertEquals(
                0,
                player.getCards()
                      .size()
        );
        verify(card).execute("1", "user");
        verify(game).setState(any(EventState.class));
    }

    @Test
    void testIsCardPlayable_AnotherDayEventCard_Playable() {
        AnotherDayEventCard card = mock(AnotherDayEventCard.class);
        when(card.getId()).thenReturn(1);
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        player.getCards()
              .add(card);
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        when(game.getPlayer("user")).thenReturn(player);

        boolean result = cardManagement.isCardPlayable(game,1, "user");

        assertTrue(result);
    }

    @Test
    void testIsCardPlayable_AnotherDayEventCard_NotPlayable() {
        AnotherDayEventCard card = mock(AnotherDayEventCard.class);
        when(card.getId()).thenReturn(1);
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        player.getCards()
              .add(card);
        when(game.getState()).thenReturn(mock(DrawCardState.class));

        boolean result = cardManagement.isCardPlayable(game,1, "user");

        assertFalse(result);
    }

    @Test
    void testIsCardPlayable_CardNotFound() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        player.getCards().clear();

        boolean result = cardManagement.isCardPlayable(game, 1, "user");

        assertFalse(result);
    }

    @Test
    void testIsCardPlayable_CardNotPlayable() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getState()).thenReturn(mock(DrawCardState.class));

        boolean result = cardManagement.isCardPlayable(game, 1, "user");

        assertFalse(result);
    }

    @Test
    void testIsCardPlayable_TreatWaterEventCard_Playable() {
        TreatWaterEventCard card = mock(TreatWaterEventCard.class);
        when(card.getId()).thenReturn(1);
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        player.getCards().add(card);
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getWaterTreatmentsLeft()).thenReturn(1);

        boolean result = cardManagement.isCardPlayable(game, 1, "user");

        assertTrue(result);
    }

    @Test
    void testIsCardPlayable_TreatWaterEventCard_NotPlayable() {
        TreatWaterEventCard card = mock(TreatWaterEventCard.class);
        when(card.getId()).thenReturn(1);
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        player.getCards().add(card);
        when(game.getState()).thenReturn(mock(DrawCardState.class));
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getWaterTreatmentsLeft()).thenReturn(0);

        boolean result = cardManagement.isCardPlayable(game, 1, "user");

        assertFalse(result);
    }

    @Test
    void testIsStateCorrect() {
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        assertTrue(cardManagement.isStateCorrect(game));

        when(game.getState()).thenReturn(mock(InfectionState.class));
        assertTrue(cardManagement.isStateCorrect(game));

        when(game.getState()).thenReturn(mock(DrawCardState.class));
        assertTrue(cardManagement.isStateCorrect(game));

        when(game.getState()).thenReturn(mock(EventState.class));
        assertFalse(cardManagement.isStateCorrect(game));
    }
}
