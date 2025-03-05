package de.uol.swp.server.player;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.player.message.request.DrawInfectionCardRequest;
import de.uol.swp.common.player.message.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.message.request.GetCardsToSortRequest;
import de.uol.swp.common.player.message.request.SortedCardsRequest;
import de.uol.swp.common.player.message.response.CardsToSortResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

public class PlayerServiceTest extends EventBusBasedTest {
    @Mock
    private IPlayerManagement playerManagement;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    private final IGame game = new Game(1, "validGameId");

    @Mock
    private DrawPlayerCardRequest request;

    @Mock
    private ICardDTO cardDTO;

    @Mock
    private Session session;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    PlayerService playerService = new PlayerService(
            getBus(),
            playerManagement,
            gameManagement,
            lobbyManagement
    );


    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onStatusResponse(StatusResponse event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onCardsToSortResponse(CardsToSortResponse event) {
        super.handleEvent(event);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        IUser user = new User("testUser", "testPassword");

        when(request.getLobbyId()).thenReturn("validGameId");

        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(request.getSession()).thenReturn(Optional.of(session));

        GameStore.getInstance().addGame("validGameId", game);
        when(playerManagement.getGame("validGameId")).thenReturn(game);
    }

    @Test
    void onDrawPlayerCardRequest_Success() throws PlayerManagementException, InterruptedException {
        when(playerManagement.drawPlayerCard(eq("validGameId"), any(IUser.class))).thenReturn(cardDTO);
        when(gameManagement.getGame("validGameId")).thenReturn(game);

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, times(1)).drawPlayerCard("validGameId", UserMapper.toUser(session.getUser()));
    }

    @Test
    void onDrawPlayerCardRequest_PlayerManagementException() throws PlayerManagementException, InterruptedException {
        when(gameManagement.getGame("validGameId")).thenReturn(game);
        doThrow(new PlayerManagementException("Error")).when(playerManagement)
                                                       .drawPlayerCard(eq("validGameId"), any(IUser.class));

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
    }

    @Test
    void onShareRideRequest() throws InterruptedException, PlayerManagementException {
        IUser user = new User("testUser", "testPassword");
        Session testSession = UUIDSession.create(user);
        ShareRideRequest shareRideRequest = new ShareRideRequest("validGameId", 1);
        shareRideRequest.setSession(testSession);

        when(gameManagement.getGame("validGameId")).thenReturn(game);

        postAndWait(shareRideRequest);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, times(1)).setPlayerLocation("validGameId", "testUser", 1);
        verify(gameManagement, times(1)).unlockGameInWaitForConfirmation("validGameId");
    }

    @Test
    void onNotConfirmedShareRideRequest() throws InterruptedException, PlayerManagementException {
        IUser user = new User("testUser", "testPassword");
        Session testSession = UUIDSession.create(user);
        ShareRideRequest shareRideRequest = new ShareRideRequest("validGameId");
        shareRideRequest.setSession(testSession);

        when(gameManagement.getGame("validGameId")).thenReturn(game);

        postAndWait(shareRideRequest);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, never()).setPlayerLocation(eq("validGameId"), eq("testUser"), anyInt());
        verify(gameManagement, times(1)).unlockGameInWaitForConfirmation("validGameId");
    }

    @Test
    void onDrawInfectionCardRequest_Success() throws CityManagementException, InterruptedException {
        DrawInfectionCardRequest request = mock(DrawInfectionCardRequest.class);
        Session session = mock(Session.class);
        InfectionCard infectionCard = mock(InfectionCard.class);
        IUser user = new User("testUser", "testPassword");
        IPlayer player = new Player(user);
        when(request.getLobbyId()).thenReturn("validGameId");
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        game.getPlayers()
            .add(player);
        player.setRole(new Sailor());
        when(gameManagement.drawInfectionCard(game)).thenReturn(infectionCard);

        postAndWait(request);

        verify(playerManagement, times(1)).getGame("validGameId");
        verify(gameManagement, times(1)).drawInfectionCard(game);
        assertInstanceOf(BoardUpdateEvent.class, super.event);
    }

    @Test
    void onDrawInfectionCardRequest_NotCurrentPlayer() throws InterruptedException {
        DrawInfectionCardRequest request = mock(DrawInfectionCardRequest.class);
        Session session = mock(Session.class);
        IUser user = new User("testUser", "testPassword");
        IUser user1 = new User("testUser1", "testPassword1");
        IPlayer player = new Player(user);
        game.getPlayers()
            .add(player);
        player.setRole(new Sailor());
        when(request.getLobbyId()).thenReturn("validGameId");
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(UserMapper.toDTO(user1));

        postAndWait(request);

        verify(playerManagement, times(1)).getGame("validGameId");
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onGetCardsToSortRequest_Success() throws GameException, IllegalGameStateException, InterruptedException {
        GetCardsToSortRequest request = mock(GetCardsToSortRequest.class);
        Session session = mock(Session.class);
        IUser user = new User("testUser", "testPassword");
        List<ICardDTO> cards = List.of(mock(ICardDTO.class));

        when(request.getLobbyId()).thenReturn("validGameId");
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(playerManagement.getCardsToSort("validGameId", user)).thenReturn(cards);

        postAndWait(request);

        verify(playerManagement, times(1)).getCardsToSort("validGameId", user);
        assertInstanceOf(CardsToSortResponse.class, super.event);
    }

    @Test
    void onSortedCardsRequest_Success() throws GameException, InterruptedException {
        SortedCardsRequest request = mock(SortedCardsRequest.class);
        Session session = mock(Session.class);
        when(authenticationService.getSession(any())).thenReturn(Optional.ofNullable(session));
        IUser user = new User("testUser", "testPassword");
        List<ICardDTO> cards = List.of(mock(ICardDTO.class));
        IGame game = new Game(1, "validGameId");

        when(request.getLobbyId()).thenReturn("validGameId");
        assert session != null;
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(request.getCards()).thenReturn(cards);
        when(playerManagement.getGame("validGameId")).thenReturn(game);
        when(lobbyManagement.getLobby("validGameId")).thenReturn(mock(ILobby.class));

        postAndWait(request);

        verify(playerManagement, times(1)).sortCards("validGameId", user, cards);
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onGetCardsToSortRequest_IllegalStateException() throws GameException, IllegalGameStateException,
            InterruptedException {
        GetCardsToSortRequest request = mock(GetCardsToSortRequest.class);
        Session session = mock(Session.class);
        IUser user = new User("testUser", "testPassword");

        when(request.getLobbyId()).thenReturn("validGameId");
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(playerManagement.getCardsToSort("validGameId", user)).thenThrow(new IllegalGameStateException("Test exception"));

        postAndWait(request);

        verify(playerManagement, times(1)).getCardsToSort("validGameId", user);
        assertInstanceOf(StatusResponse.class, super.event);
    }

    @Test
    void onSortedCardsRequest_IllegalStateException() throws GameException, InterruptedException {
        SortedCardsRequest request = mock(SortedCardsRequest.class);
        Session session = mock(Session.class);
        IUser user = new User("testUser", "testPassword");
        List<ICardDTO> cards = List.of(mock(ICardDTO.class));
        IGame game = new Game(1, "validGameId");

        when(request.getLobbyId()).thenReturn("validGameId");
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(request.getCards()).thenReturn(cards);
        when(playerManagement.getGame("validGameId")).thenReturn(game);
        when(lobbyManagement.getLobby("validGameId")).thenReturn(mock(ILobby.class));

        doThrow(new IllegalStateException("Test exception")).when(playerManagement).sortCards("validGameId", user, cards);

        postAndWait(request);

        verify(playerManagement, times(1)).sortCards("validGameId", user, cards);
        assertInstanceOf(StatusResponse.class, super.event);
    }
}