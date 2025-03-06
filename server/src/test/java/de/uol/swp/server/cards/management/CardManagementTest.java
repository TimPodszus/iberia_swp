package de.uol.swp.server.cards.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.CardRepository;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.*;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.InfectionState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
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

    private ICardManagement cardManagement;

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
     *
     * @throws CardNotPlayableException if the card cannot be played
     */
    @Test
    void testPlayCard() throws CardNotPlayableException {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getPlayerCardDiscardPile()).thenReturn(new ArrayList<>());

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
     *
     * @throws CardNotPlayableException if the card cannot be played
     */
    @Test
    void testPlayEventCard() throws CardNotPlayableException {
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

    /**
     * Tests the playCard method when the game is in the WaitForPositioning state.
     */
    @Test
    void testPlayCard_inWaitForPositioningState() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getState()).thenReturn(new WaitForPositioning());
        EventCard card = mock(EventCard.class);
        when(card.getId()).thenReturn(1);
        player.setCards(new ArrayList<>(List.of(card)));

        assertThrows(CardNotPlayableException.class, () -> cardManagement.playCard("1", "user", 1));
    }

    /**
     * Tests the playCard method for a card that is not in the player's hand.
     */
    @Test
    void testPlayEventCardWithWrongId() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        player.setCards(new ArrayList<>(List.of()));

        assertThrows(CardNotPlayableException.class, () -> cardManagement.playCard("1", "user", 1));
    }

    /**
     * Tests the playCard method for a StateMobilizationEventCard.
     *
     * @throws CardNotPlayableException if the card cannot be played
     */
    @Test
    void testPlayEventCardWithStateMobilization() throws CardNotPlayableException {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getPlayers()).thenReturn(List.of(player));

        StateMobilizationEventCard card = mock(StateMobilizationEventCard.class);
        when(card.getId()).thenReturn(1);
        player.setCards(new ArrayList<>(List.of(card)));
        when(game.getState()).thenReturn(new PlayerTurnState());

        cardManagement.playCard("1", "user", 1);

        assertEquals(
                0,
                player.getCards()
                      .size()
        );
        verify(card, times(1)).setPlayersToMove(any());
        verify(card, times(1)).execute("1", "user");
        verify(game).setState(any(EventState.class));
    }

    /**
     * Tests the playCard method for an AnotherDayEventCard.
     *
     * @throws CardNotPlayableException if the card cannot be played
     */
    @Test
    void testPlayEventCardWithAnotherDay() throws CardNotPlayableException {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getState()).thenReturn(new PlayerTurnState());

        AnotherDayEventCard card = mock(AnotherDayEventCard.class);
        when(card.getId()).thenReturn(1);
        player.setCards(new ArrayList<>(List.of(card)));

        cardManagement.playCard("1", "user", 1);

        assertEquals(
                0,
                player.getCards()
                      .size()
        );
        verify(card, times(1)).execute("1", "user");
        verify(game).setState(any(EventState.class));
    }

    /**
     * Tests the playCard method for an AnotherDayEventCard when the game is not in the PlayerTurnState.
     */
    @Test
    void testPlayEventCardWithAnotherDayNotInPlayerTurnState() {
        IUser user = new User("user", "password");
        IPlayer player = new Player(user);
        when(game.getPlayer("user")).thenReturn(player);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getState()).thenReturn(mock(EventState.class));

        AnotherDayEventCard card = mock(AnotherDayEventCard.class);
        when(card.getId()).thenReturn(1);
        player.setCards(new ArrayList<>(List.of(card)));

        assertThrows(CardNotPlayableException.class, () -> cardManagement.playCard("1", "user", 1));
    }

    /**
     * Tests the playSecondChanceCard method when the card is present in the discard pile.
     *
     * @throws CardNotFoundException if the card is not found in the discard pile
     */
    @Test
    void testPlaySecondChanceCard_cardIsPresent() throws CardNotFoundException {
        IPlayer player = mock(Player.class);
        when(game.getPlayer("user")).thenReturn(player);
        ICity currentPosition = new City(1, PlagueName.CHOLERA, CityName.ALICANTE, 1, true);
        when(player.getCurrentPosition()).thenReturn(currentPosition);
        ICard card = new CityCard(1, currentPosition.getName().getDisplayName(), currentPosition);
        when(game.getPlayerCardDiscardPile()).thenReturn(
                new ArrayList<>(List.of(card))
        );

        cardManagement.playSecondChanceCard("1", "user");

        verify(player, times(1)).addCard(card);
        assertTrue(game.getPlayerCardDiscardPile().isEmpty());
    }

    /**
     * Tests the playSecondChanceCard method when the card is not present in the discard pile.
     */
    @Test
    void testPlaySecondChanceCard_cardIsNotPresent() {
        IPlayer player = mock(Player.class);
        when(game.getPlayer("user")).thenReturn(player);
        ICity currentPosition = new City(1, PlagueName.CHOLERA, CityName.ALICANTE, 1, true);
        when(player.getCurrentPosition()).thenReturn(currentPosition);
        when(game.getPlayerCardDiscardPile()).thenReturn(
                new ArrayList<>()
        );

        assertThrows(CardNotFoundException.class, () -> cardManagement.playSecondChanceCard("1", "user"));
    }

    /**
     * Tests the returnLastPlayedCard method.
     */
    @Test
    void testReturnLastPlayedCard() {
        IPlayer player = mock(Player.class);
        when(game.getPlayer("user")).thenReturn(player);
        ICard card = mock(ICard.class);
        when(game.getPlayerCardDiscardPile()).thenReturn(
                new ArrayList<>(List.of(card))
        );

        cardManagement.returnLastPlayedCard("1", "user");

        assertTrue(game.getPlayerCardDiscardPile().isEmpty());
        verify(player).addCard(card);
    }

    /**
     * Tests the isCardPlayable method for a TreatWaterEventCard when it is playable.
     */
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

    /**
     * Tests the isCardPlayable method for a TreatWaterEventCard when it is not playable.
     */
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

    /**
     * Tests the isStateCorrect method.
     */
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

    /**
     * Tests the getCardsFromPlayerDiscardPile method.
     */
    @Test
    void testGetCardsFromDiscardPile() {
        CardRepository cardRepository = new CardRepository(new CityRepository());
        when(game.getPlayerCardDiscardPile()).thenReturn(cardRepository.getCards()
                                                                       .values()
                                                                       .stream()
                                                                       .toList());

        List<ICard> allCards = cardManagement.getCardsFromPlayerDiscardPile(LOBBY_ID, ICard.class);
        List<CityCard> cityCards = cardManagement.getCardsFromPlayerDiscardPile(LOBBY_ID, CityCard.class);

        assertEquals(100, allCards.size());
        assertEquals(48, cityCards.size());
    }

    @Test
    void testGetCardForPlayer() throws IllegalGameStateException, CardNotFoundException {
        IPlayer player = mock(Player.class);
        when(game.getPlayer("user")).thenReturn(player);
        EventCard card = new StateMobilizationEventCard(2);
        when(game.getPlayerCardDiscardPile()).thenReturn(new ArrayList<>(List.of(card)));
        EventState eventState = new EventState(new ForTheGoodCauseEventCard(1));
        when(game.getState()).thenReturn(eventState);

        cardManagement.getCardForPlayer("1", "user", 2);

        verify(player).addCard(card);
        assertTrue(game.getPlayerCardDiscardPile()
                       .isEmpty());
    }

    @Test
    void testGetCardForPlayer_WrongState() {
        when(game.getState()).thenReturn(new PlayerTurnState());

        assertThrows(IllegalGameStateException.class, () -> cardManagement.getCardForPlayer("1", "user", 2));
    }

    @Test
    void testGetCardForPlayer_CardNotFound() {
        IPlayer player = mock(Player.class);
        when(game.getPlayer("user")).thenReturn(player);
        EventCard card = new StateMobilizationEventCard(2);
        when(game.getPlayerCardDiscardPile()).thenReturn(new ArrayList<>(List.of(card)));
        EventState eventState = new EventState(new ForTheGoodCauseEventCard(1));
        when(game.getState()).thenReturn(eventState);

        assertThrows(CardNotFoundException.class, () -> cardManagement.getCardForPlayer("1", "user", 3));
    }
}
