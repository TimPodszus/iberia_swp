package de.uol.swp.server.player;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.player.message.request.DrawInfectionCardRequest;
import de.uol.swp.common.player.message.request.DrawPlayerCardRequest;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.management.CityManagementException;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.role.Sailor;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

public class PlayerServiceTest extends EventBusBasedTest {
    @Mock
    private IPlayerManagement playerManagement;

    @Mock
    private IGameManagement gameManagement;

    private final IGame game = new Game(1, "validGameId");

    @Mock
    private DrawPlayerCardRequest request;

    @Mock
    private ICardDTO cardDTO;

    @Mock
    private Session session;

    private PlayerService playerService;

    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onStatusResponse(StatusResponse event) {
        super.handleEvent(event);
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(super.getBus(), playerManagement, gameManagement);
        IUser user = new User("testUser", "testPassword");

        when(request.getLobbyId()).thenReturn("validGameId");

        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(request.getSession()).thenReturn(Optional.of(session));

        GameStore.getInstance()
                 .addGame("validGameId", game);
    }

    @Test
    void onDrawPlayerCardRequest_Success() throws PlayerManagementException, InterruptedException {
        IGame game = mock(IGame.class);
        IPlayer mockPlayer = mock(IPlayer.class);
        IUser mockUser = mock(IUser.class);
        CityRepository mockCityRepository = mock(CityRepository.class);
        ConnectionRepository mockConnectionRepository = mock(ConnectionRepository.class);
        RegionRepository mockRegionRepository = mock(RegionRepository.class);
        PlagueRepository mockPlagueRepository = mock(PlagueRepository.class);
        IRole mockRole = mock(IRole.class);
        IGameState iGameState = mock(IGameState.class);

        when(game.getPlayers()).thenReturn(List.of(mockPlayer));
        when(game.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(game.getCityRepository()).thenReturn(mockCityRepository);
        when(mockCityRepository.getCities()).thenReturn(Collections.emptyList());
        when(game.getConnectionRepository()).thenReturn(mockConnectionRepository);
        when(mockConnectionRepository.getConnections()).thenReturn(Collections.emptyList());
        when(game.getRegionRepository()).thenReturn(mockRegionRepository);
        when(mockRegionRepository.getRegions()).thenReturn(Collections.emptyList());
        when(game.getPlagueRepository()).thenReturn(mockPlagueRepository);
        when(mockPlagueRepository.getPlagues()).thenReturn(Collections.emptyList());
        when(game.getCurrentPlayer().getRole()).thenReturn(mockRole);
        when(game.getState()).thenReturn(iGameState);


        when(playerManagement.drawPlayerCard(eq("validGameId"), any(IUser.class))).thenReturn(cardDTO);
        when(gameManagement.getGame("validGameId")).thenReturn(game);

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, super.event);
        verify(playerManagement, times(1)).drawPlayerCard("validGameId", UserMapper.toUser(session.getUser()));
    }

    @Test
    void onDrawPlayerCardRequest_PlayerManagementException() throws PlayerManagementException, InterruptedException {
        IGame game = mock(IGame.class);
        IPlayer mockPlayer = mock(IPlayer.class);
        IUser mockUser = mock(IUser.class);
        CityRepository mockCityRepository = mock(CityRepository.class);
        ConnectionRepository mockConnectionRepository = mock(ConnectionRepository.class);
        RegionRepository mockRegionRepository = mock(RegionRepository.class);
        PlagueRepository mockPlagueRepository = mock(PlagueRepository.class);
        IRole mockRole = mock(IRole.class);
        IGameState iGameState = mock(IGameState.class);

        when(game.getPlayers()).thenReturn(List.of(mockPlayer));
        when(game.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(game.getCityRepository()).thenReturn(mockCityRepository);
        when(mockCityRepository.getCities()).thenReturn(Collections.emptyList());
        when(game.getConnectionRepository()).thenReturn(mockConnectionRepository);
        when(mockConnectionRepository.getConnections()).thenReturn(Collections.emptyList());
        when(game.getRegionRepository()).thenReturn(mockRegionRepository);
        when(mockRegionRepository.getRegions()).thenReturn(Collections.emptyList());
        when(game.getPlagueRepository()).thenReturn(mockPlagueRepository);
        when(mockPlagueRepository.getPlagues()).thenReturn(Collections.emptyList());
        when(game.getCurrentPlayer().getRole()).thenReturn(mockRole);
        when(game.getState()).thenReturn(iGameState);

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

        IGame game = mock(IGame.class);
        IPlayer mockPlayer = mock(IPlayer.class);
        CityRepository mockCityRepository = mock(CityRepository.class);
        ConnectionRepository mockConnectionRepository = mock(ConnectionRepository.class);
        RegionRepository mockRegionRepository = mock(RegionRepository.class);
        PlagueRepository mockPlagueRepository = mock(PlagueRepository.class);
        IRole mockRole = mock(IRole.class);
        IGameState iGameState = mock(IGameState.class);

        when(game.getPlayers()).thenReturn(List.of(mockPlayer));
        when(game.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getUser()).thenReturn(user);
        when(game.getCityRepository()).thenReturn(mockCityRepository);
        when(mockCityRepository.getCities()).thenReturn(Collections.emptyList());
        when(game.getConnectionRepository()).thenReturn(mockConnectionRepository);
        when(mockConnectionRepository.getConnections()).thenReturn(Collections.emptyList());
        when(game.getRegionRepository()).thenReturn(mockRegionRepository);
        when(mockRegionRepository.getRegions()).thenReturn(Collections.emptyList());
        when(game.getPlagueRepository()).thenReturn(mockPlagueRepository);
        when(mockPlagueRepository.getPlagues()).thenReturn(Collections.emptyList());
        when(game.getCurrentPlayer().getRole()).thenReturn(mockRole);
        when(game.getState()).thenReturn(iGameState);

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

        IGame game = mock(IGame.class);
        IPlayer mockPlayer = mock(IPlayer.class);
        CityRepository mockCityRepository = mock(CityRepository.class);
        ConnectionRepository mockConnectionRepository = mock(ConnectionRepository.class);
        RegionRepository mockRegionRepository = mock(RegionRepository.class);
        PlagueRepository mockPlagueRepository = mock(PlagueRepository.class);
        IRole mockRole = mock(IRole.class);
        IGameState iGameState = mock(IGameState.class);

        when(game.getPlayers()).thenReturn(List.of(mockPlayer));
        when(game.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getUser()).thenReturn(user);
        when(game.getCityRepository()).thenReturn(mockCityRepository);
        when(mockCityRepository.getCities()).thenReturn(Collections.emptyList());
        when(game.getConnectionRepository()).thenReturn(mockConnectionRepository);
        when(mockConnectionRepository.getConnections()).thenReturn(Collections.emptyList());
        when(game.getRegionRepository()).thenReturn(mockRegionRepository);
        when(mockRegionRepository.getRegions()).thenReturn(Collections.emptyList());
        when(game.getPlagueRepository()).thenReturn(mockPlagueRepository);
        when(mockPlagueRepository.getPlagues()).thenReturn(Collections.emptyList());
        when(game.getCurrentPlayer().getRole()).thenReturn(mockRole);
        when(game.getState()).thenReturn(iGameState);

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
        when(gameManagement.getGame("validGameId")).thenReturn(game);
        when(gameManagement.drawInfectionCard(game)).thenReturn(infectionCard);

        postAndWait(request);

        verify(gameManagement, times(1)).getGame("validGameId");
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
        when(gameManagement.getGame("validGameId")).thenReturn(game);

        postAndWait(request);

        verify(gameManagement, times(1)).getGame("validGameId");
        assertInstanceOf(StatusResponse.class, super.event);
    }
}