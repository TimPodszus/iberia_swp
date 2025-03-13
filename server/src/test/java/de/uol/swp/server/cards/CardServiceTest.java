package de.uol.swp.server.cards;

import de.uol.swp.common.cards.request.GetCardRequest;
import de.uol.swp.common.cards.request.PlayCardRequest;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.CardSelectionEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.cards.events.ForTheGoodCauseEvent;
import de.uol.swp.server.cards.events.SecondChanceEvent;
import de.uol.swp.server.cards.management.CardNotFoundException;
import de.uol.swp.server.cards.management.CardNotPlayableException;
import de.uol.swp.server.cards.management.ICardManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import de.uol.swp.server.usermanagement.management.ServerUserService;
import de.uol.swp.server.usermanagement.management.UserManagement;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for CardService.
 */
public class CardServiceTest extends EventBusBasedTest {

    @Mock
    private ICardManagement cardManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ServerUserService userService;

    @Mock
    private UserManagement userManagement;

    @InjectMocks
    private CardService cardService = new CardService(getBus(), cardManagement, lobbyManagement, userService);

    /**
     * Handles BoardUpdateEvent.
     *
     * @param event the BoardUpdateEvent to handle
     */
    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    /**
     * Handles StatusResponse.
     *
     * @param event the StatusResponse to handle
     */
    @Subscribe
    public void onStatusResponse(StatusResponse event) {
        super.handleEvent(event);
    }

    /**
     * Handles CardSelectionEvent
     *
     * @param event the CardSelectionEvent to handle
     */
    @Subscribe
    public void onCardSelectionEvent(CardSelectionEvent event) {
        super.handleEvent(event);
    }

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the onPlayCardRequest method with a valid session.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnPlayCardRequest() throws InterruptedException, CardNotPlayableException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        ILobby lobby = new Lobby("1234", "testLobby", new ArrayList<>(List.of(user)), user, 2);
        when(lobbyManagement.getLobby("1234")).thenReturn(lobby);

        IGame game = new Game(2, "1234");
        when(cardManagement.getGame("1234")).thenReturn(game);

        PlayCardRequest request = new PlayCardRequest("1234", 1);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, event, "Expected BoardUpdateEvent");
        verify(cardManagement, atLeastOnce()).playCard("1234", "testuser", 1);
    }

    /**
     * Tests the onPlayCardRequest method with a valid session.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnPlayCardRequest_withWrongCard() throws InterruptedException, CardNotPlayableException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        ILobby lobby = new Lobby("1234", "testLobby", new ArrayList<>(List.of(user)), user, 2);
        when(lobbyManagement.getLobby("1234")).thenReturn(lobby);

        IGame game = new Game(2, "1234");
        when(cardManagement.getGame("1234")).thenReturn(game);
        doThrow(new CardNotPlayableException()).when(cardManagement)
                                               .playCard("1234", "testuser", 1);

        PlayCardRequest request = new PlayCardRequest("1234", 1);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(StatusResponse.class, event, "Expected StatusResponse");
        verify(cardManagement, atLeastOnce()).playCard("1234", "testuser", 1);
    }

    /**
     * Tests the onPlayCardRequest method with a missing session.
     */
    @Test
    void testOnPlayCardRequestWithMissingSession() {
        PlayCardRequest request = new PlayCardRequest("1234", 1);
        assertThrows(SessionNotFoundException.class, () -> cardService.onPlayCardRequest(request));
    }

    @Test
    void testOnSecondChanceEvent() throws CardNotFoundException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        ILobby lobby = new Lobby("1234", "testLobby", new ArrayList<>(List.of(user)), user, 2);
        when(lobbyManagement.getLobby("1234")).thenReturn(lobby);

        IGame game = new Game(2, "1234");
        when(cardManagement.getGame("1234")).thenReturn(game);

        SecondChanceEvent event = new SecondChanceEvent("1234", "testuser");
        event.setSession(session);

        postAndWait(event);

        assertInstanceOf(BoardUpdateEvent.class, this.event, "Expected BoardUpdateEvent");
        verify(cardManagement, atLeastOnce()).playSecondChanceCard("1234", "testuser");
    }

    @Test
    void testOnSecondChanceEvent_withMissingSession() {
        SecondChanceEvent event = new SecondChanceEvent("1234", "testuser");
        assertThrows(SessionNotFoundException.class, () -> cardService.onSecondChanceEvent(event));
    }

    @Test
    void testOnSecondChanceEvent_withCardNotFoundException() throws CardNotFoundException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        ILobby lobby = new Lobby("1234", "testLobby", new ArrayList<>(List.of(user)), user, 2);
        when(lobbyManagement.getLobby("1234")).thenReturn(lobby);

        IGame game = new Game(2, "1234");
        when(cardManagement.getGame("1234")).thenReturn(game);
        doThrow(new CardNotFoundException("CityCard not found in discard pile")).when(cardManagement)
                                                                                .playSecondChanceCard(
                                                                                        "1234",
                                                                                        "testuser"
                                                                                );

        SecondChanceEvent event = new SecondChanceEvent("1234", "testuser");
        event.setSession(session);

        postAndWait(event);

        assertInstanceOf(BoardUpdateEvent.class, this.event, "Expected BoardUpdateEvent");
        verify(cardManagement, atLeastOnce()).returnLastPlayedCard("1234", "testuser");
    }

    /**
     * Tests the onForTheGoodCauseEvent method.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnForTheGoodCauseEvent() throws InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        EventCard eventCard = new AnotherDayEventCard(1);
        when(cardManagement.getCardsFromPlayerDiscardPile("lobbyId", EventCard.class)).thenReturn(List.of(eventCard));

        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("lobbyId", "testuser");
        postAndWait(forTheGoodCauseEvent);

        assertInstanceOf(CardSelectionEvent.class, this.event, "Expected CardSelectionEvent");
    }

    /**
     * Tests the onForTheGoodCauseEvent method when the session is not found.
     */
    @Test
    void testOnForTheGoodCauseEvent_SessionNotFound() {
        IUser user = new User("testuser", "testpassword");
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.empty());

        when(cardManagement.getCardsFromPlayerDiscardPile("lobbyId", EventCard.class)).thenReturn(List.of());
        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("lobbyId", "testuser");

        assertThrows(SessionNotFoundException.class, () -> cardService.onForTheGoodCauseEvent(forTheGoodCauseEvent));
    }

    @Test
    void testOnForTheGoodCauseEvent_NoEventCardsFound() throws InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));
        when(cardManagement.getCardsFromPlayerDiscardPile("lobbyId", EventCard.class)).thenReturn(List.of());

        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("lobbyId", "testuser");
        postAndWait(forTheGoodCauseEvent);

        assertInstanceOf(StatusResponse.class, this.event, "Expected CardSelectionEvent");
        assertFalse(((StatusResponse) this.event).isSuccess());
        verify(cardManagement, times(1)).returnLastPlayedCard("lobbyId", "testuser");
    }

    /**
     * Tests the GetCardRequest.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnGetCardRequest() throws IllegalGameStateException, CardNotFoundException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        IGame game = new Game(2, "lobbyId");
        when(cardManagement.getGame("lobbyId")).thenReturn(game);

        ILobby lobby = new Lobby("lobbyId", "testLobby", new ArrayList<>(List.of(user)), user, 2);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        GetCardRequest request = new GetCardRequest("lobbyId", 1);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, this.event, "Expected BoardUpdateEvent");
        verify(cardManagement, times(1)).getCardForPlayer("lobbyId", "testuser", 1);
    }

    /**
     * Tests the onGetCardRequest method with a missing session.
     */
    @Test
    void testOnGetCardRequest_MissingSession() {
        GetCardRequest request = new GetCardRequest("lobbyId", 1);
        assertThrows(SessionNotFoundException.class, () -> cardService.onGetCardRequest(request));
    }

    /**
     * Tests the onGetCardRequest method with a card not found exception.
     *
     * @throws InterruptedException      if the thread is interrupted
     * @throws IllegalGameStateException if the game is in an illegal state
     * @throws CardNotFoundException     if the card is not found
     */
    @Test
    void testOnGetCardRequest_CardNotFoundException() throws InterruptedException, IllegalGameStateException, CardNotFoundException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        doThrow(new CardNotFoundException("Card not found")).when(cardManagement)
                                                            .getCardForPlayer("lobbyId", "testuser", 1);

        GetCardRequest request = new GetCardRequest("lobbyId", 1);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(StatusResponse.class, this.event);
        assertFalse(((StatusResponse) this.event).isSuccess());
    }

    /**
     * Tests the onGetCardRequest method with an illegal game state.
     *
     * @throws InterruptedException      if the thread is interrupted
     * @throws IllegalGameStateException if the game is in an illegal state
     * @throws CardNotFoundException     if the card is not found
     */
    @Test
    void testGetCardRequest_IllegalGameState() throws InterruptedException, IllegalGameStateException, CardNotFoundException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        doThrow(new IllegalGameStateException("Game is not in the right state")).when(cardManagement)
                                                                                .getCardForPlayer(
                                                                                        "lobbyId",
                                                                                        "testuser",
                                                                                        1
                                                                                );
        GetCardRequest request = new GetCardRequest("lobbyId", 1);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }
}
