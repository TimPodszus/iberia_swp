package de.uol.swp.server.player;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.player.message.event.DiscardPlayerCardEvent;
import de.uol.swp.common.player.message.request.*;
import de.uol.swp.common.player.message.response.CardsToSortResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.management.CityManagementException;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.role.Sailor;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

public class PlayerServiceTest extends EventBusBasedTest {
    private static final String LOBBY_ID = "validGameId";

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private IPlayerManagement playerManagement;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    private final IGame game = new Game(1, LOBBY_ID);

    @Mock
    private ICardDTO cardDTO;

    private final IUser user = new User("testUser", "testPassword");

    private final Session session = UUIDSession.create(user);

    private PlayerService playerService;

    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onStatusResponse(StatusResponse event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onDiscardPlayerCardEvent(DiscardPlayerCardEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onCardsToSortResponse(CardsToSortResponse event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onGetCardsToSortRequest(GetCardsToSortRequest event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onSortedCardsRequest(SortedCardsRequest event) {
        super.handleEvent(event);
    }

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(super.getBus(), playerManagement, gameManagement, lobbyManagement);

        GameStore.getInstance()
                 .addGame(LOBBY_ID, game);

        Field authServiceField = AbstractService.class.getDeclaredField("authenticationService");
        authServiceField.setAccessible(true);
        authServiceField.set(playerService, authenticationService);

        when(playerManagement.getGame(LOBBY_ID)).thenReturn(game);
    }

    @Test
    void onDrawPlayerCardRequest_Success() throws IllegalGameStateException, InterruptedException {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest(LOBBY_ID);
        request.setSession(session);

        when(playerManagement.drawPlayerCard(eq(LOBBY_ID), any(IUser.class))).thenReturn(cardDTO);

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, times(1)).drawPlayerCard(LOBBY_ID, UserMapper.toUser(session.getUser()));
    }

    @Test
    void onDrawPlayerCardRequest_IllegalGameStateException() throws IllegalGameStateException, InterruptedException {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest(LOBBY_ID);
        request.setSession(session);

        doThrow(new IllegalGameStateException("Error")).when(playerManagement)
                                                       .drawPlayerCard(eq(LOBBY_ID), any(IUser.class));

        postAndWait(request);

        assertInstanceOf(StatusResponse.class, super.event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    @Test
    void onShareRideRequest() throws InterruptedException, PlayerManagementException {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID, 1);
        shareRideRequest.setSession(session);

        postAndWait(shareRideRequest);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, times(1)).setPlayerLocation(LOBBY_ID, "testUser", 1);
        verify(gameManagement, times(1)).unlockGameInWaitForConfirmation(LOBBY_ID);
    }

    @Test
    void onNotConfirmedShareRideRequest() throws InterruptedException, PlayerManagementException {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID);
        shareRideRequest.setSession(session);

        postAndWait(shareRideRequest);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, never()).setPlayerLocation(eq(LOBBY_ID), eq("testUser"), anyInt());
        verify(gameManagement, times(1)).unlockGameInWaitForConfirmation(LOBBY_ID);
    }

    @Test
    void onDrawInfectionCardRequest_Success() throws CityManagementException, InterruptedException {
        DrawInfectionCardRequest drawInfectionCardRequest = new DrawInfectionCardRequest(LOBBY_ID);
        drawInfectionCardRequest.setSession(session);

        InfectionCard infectionCard = mock(InfectionCard.class);
        IPlayer player = new Player(user);
        player.setRole(new Sailor());
        game.getPlayers()
            .add(player);

        when(gameManagement.drawInfectionCard(game)).thenReturn(infectionCard);

        postAndWait(drawInfectionCardRequest);

        verify(playerManagement, times(1)).getGame(LOBBY_ID);
        verify(gameManagement, times(1)).drawInfectionCard(game);
        assertInstanceOf(BoardUpdateEvent.class, super.event);
    }

    @Test
    void onDrawInfectionCardRequest_NotCurrentPlayer() throws InterruptedException {
        DrawInfectionCardRequest drawInfectionCardRequest = new DrawInfectionCardRequest(LOBBY_ID);
        IUser user1 = new User("testUser1", "testPassword1");
        Session session1 = UUIDSession.create(user1);
        drawInfectionCardRequest.setSession(session1);

        IPlayer player = new Player(user);
        game.getPlayers()
            .add(player);
        player.setRole(new Sailor());

        postAndWait(drawInfectionCardRequest);

        verify(playerManagement, times(1)).getGame(LOBBY_ID);
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onDiscardPlayerCardRequest_Success() throws InterruptedException, GameException {
        ICardDTO card = mock(ICardDTO.class);
        DiscardPlayerCardRequest discardPlayerCardRequest = new DiscardPlayerCardRequest(LOBBY_ID, card);
        discardPlayerCardRequest.setSession(session);

        postAndWait(discardPlayerCardRequest);

        verify(playerManagement, times(1)).discardPlayerCard(eq(LOBBY_ID), anyString(), anyInt());
        assertInstanceOf(BoardUpdateEvent.class, super.event);
    }

    @Test
    void onDiscardPlayerCardRequest_GameException() throws InterruptedException, GameException {
        ICardDTO card = mock(ICardDTO.class);
        DiscardPlayerCardRequest discardPlayerCardRequest = new DiscardPlayerCardRequest(LOBBY_ID, card);
        discardPlayerCardRequest.setSession(session);
        doThrow(new GameException("Error")).when(playerManagement)
                                           .discardPlayerCard(any(), any(), anyInt());

        postAndWait(discardPlayerCardRequest);

        verify(playerManagement, times(1)).discardPlayerCard(any(), any(), anyInt());
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onCardsAmountChanged_ExceedsLimit_SendsDiscardPlayerCardEvent() throws NoSuchFieldException, IllegalAccessException {
        playerService = Mockito.spy(new PlayerService(getBus(), playerManagement, gameManagement, lobbyManagement));

        IPlayer player = new Player(user);
        game.getPlayers()
            .add(player);

        List<ICard> cardList = new ArrayList<>(Collections.nCopies(8, mock(ICard.class)));
        player.getCards()
              .addAll(cardList);

        Field authServiceField = AbstractService.class.getDeclaredField("authenticationService");
        authServiceField.setAccessible(true);
        authServiceField.set(playerService, authenticationService);

        when(authenticationService.getSession(any(IUser.class))).thenReturn(Optional.of(session));

        playerService.onCardsAmountChanged(LOBBY_ID, user.getUsername(), Collections.emptyList());

        assertInstanceOf(DiscardPlayerCardEvent.class, super.event);
    }

    @Test
    void onGetCardsToSortRequest_Success() throws GameException, IllegalGameStateException, InterruptedException {
        GetCardsToSortRequest request = new GetCardsToSortRequest(LOBBY_ID);
        request.setSession(session);
        List<ICardDTO> cards = List.of(mock(ICardDTO.class));

        when(playerManagement.getCardsToSort("validGameId", user)).thenReturn(cards);

        postAndWait(request);

        verify(playerManagement, times(1)).getCardsToSort("validGameId", user);
        assertInstanceOf(CardsToSortResponse.class, super.event);
    }

    @Test
    void onSortedCardsRequest_Success() throws InterruptedException, IllegalGameStateException {
        List<ICardDTO> cards = List.of(mock(ICardDTO.class));
        SortedCardsRequest request = new SortedCardsRequest(LOBBY_ID, cards);
        request.setSession(session);
        when(authenticationService.getSession(any())).thenReturn(Optional.of(session));
        when(lobbyManagement.getLobby(LOBBY_ID)).thenReturn(mock(ILobby.class));

        postAndWait(request);

        verify(playerManagement, times(1)).sortCards(LOBBY_ID, UserMapper.toUser(session.getUser()), cards);
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onGetCardsToSortRequest_IllegalStateException() throws GameException, IllegalGameStateException,
            InterruptedException {
        GetCardsToSortRequest request = new GetCardsToSortRequest(LOBBY_ID);
        request.setSession(session);

        when(playerManagement.getCardsToSort(LOBBY_ID, user)).thenThrow(new IllegalGameStateException("Test exception"));

        postAndWait(request);

        verify(playerManagement, times(1)).getCardsToSort(LOBBY_ID, user);
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onSortedCardsRequest_IllegalGameStateException() throws InterruptedException, IllegalGameStateException {
        List<ICardDTO> cards = List.of(mock(ICardDTO.class));
        SortedCardsRequest request = new SortedCardsRequest(LOBBY_ID, cards);
        request.setSession(session);

        when(lobbyManagement.getLobby(LOBBY_ID)).thenReturn(mock(ILobby.class));

        doThrow(new IllegalGameStateException("Test exception")).when(playerManagement)
                                                                .sortCards("validGameId", user, cards);

        postAndWait(request);

        verify(playerManagement, times(1)).sortCards(LOBBY_ID, user, cards);
        assertInstanceOf(StatusResponse.class, super.event);
    }
}