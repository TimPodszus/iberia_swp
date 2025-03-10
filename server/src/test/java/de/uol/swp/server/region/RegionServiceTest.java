package de.uol.swp.server.region;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.message.request.AvailableRegionsRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentEventRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRequest;
import de.uol.swp.common.region.message.response.TreatWaterEventResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.events.TreatWaterEvent;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlaceExtraWaterTreatmentState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegionServiceTest extends EventBusBasedTest {

    @Mock
    private IRegionManagement regionManagement;

    @Mock
    private IPlayerManagement playerManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    RegionService regionService = new RegionService(getBus(), regionManagement, playerManagement, lobbyManagement);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Subscribe
    public void onAvailableRegionsRequest(AvailableRegionsRequest request) {
        super.handleEvent(request);
    }

    @Subscribe
    public void onWaterTreatmentRegionRequest(WaterTreatmentRegionRequest request) {
        super.handleEvent(request);
    }

    @Subscribe
    public void onWaterTreatmentRequest(WaterTreatmentRequest request) {
        super.handleEvent(request);
    }

    @Subscribe
    public void onWaterTreatmentEventRequest(WaterTreatmentRequest request) {
        super.handleEvent(request);
    }

    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onStatusResponse(StatusResponse response) {
        super.handleEvent(response);
    }

    @Subscribe
    public void onTreatWaterEventResponse(TreatWaterEventResponse response) {
        super.handleEvent(response);
    }

    @Test
    void testOnSendAvailableRegionsRequest_UserUnknown() {
        AvailableRegionsRequest availableRegionsRequest = new AvailableRegionsRequest("testLobbyId");

        assertThrows(SessionNotFoundException.class,
                () -> regionService.onSendAvailableRegionsRequest(availableRegionsRequest)
        );
    }

    @Test
    void testOnSendAvailableRegionsRequest_Success() throws InterruptedException {
        AvailableRegionsRequest availableRegionsRequest = mock(AvailableRegionsRequest.class);
        Session session = mock(Session.class);
        IUserDTO userDTO = mock(IUserDTO.class);
        Set<IRegionDTO> regions = mock(Set.class);
        String lobbyId = "testLobbyId";

        when(availableRegionsRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(userDTO);
        when(availableRegionsRequest.getLobbyId()).thenReturn(lobbyId);
        when(regionManagement.getAvailableRegions(userDTO, lobbyId)).thenReturn(regions);

        postAndWait(availableRegionsRequest);

        verify(regionManagement, times(1)).getAvailableRegions(userDTO, lobbyId);
    }

    @Test
    void testOnWaterTreatmentRegionRequest_UserUnknown() {
        WaterTreatmentRegionRequest waterTreatmentRegionRequest = new WaterTreatmentRegionRequest("testLobbyId", 1);

        assertThrows(SessionNotFoundException.class,
                () -> regionService.onWaterTreatmentRegionRequest(waterTreatmentRegionRequest)
        );
    }

    @Test
    void testOnWaterTreatmentRegionRequest_Success() throws InterruptedException {
        WaterTreatmentRegionRequest waterTreatmentRegionRequest = mock(WaterTreatmentRegionRequest.class);
        Session session = mock(Session.class);
        IUserDTO user = mock(IUserDTO.class);
        IGame game = mock(IGame.class);
        List<CityCardDTO> cityCards = mock(List.class);

        String lobbyId = "testLobbyId";
        when(waterTreatmentRegionRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(user);
        when(waterTreatmentRegionRequest.getLobbyId()).thenReturn(lobbyId);
        when(regionManagement.getGame(lobbyId)).thenReturn(game);
        when(regionManagement.getPossibleCityCardsToDiscard(
                user,
                waterTreatmentRegionRequest.getRegionId(),
                lobbyId
        )).thenReturn(cityCards);

        postAndWait(waterTreatmentRegionRequest);

        verify(regionManagement, times(1)).getPossibleCityCardsToDiscard(
                user,
                waterTreatmentRegionRequest.getRegionId(),
                lobbyId
        );
    }

    @Test
    void testOnSendWaterTreatmentRequest_UserUnknown() {
        WaterTreatmentRequest waterTreatmentRequest = new WaterTreatmentRequest("testLobbyId", 1, 5, null);

        assertThrows(SessionNotFoundException.class,
                () -> regionService.onSendWaterTreatmentRequest(waterTreatmentRequest)
        );
    }

    @Test
    void testOnSendWaterTreatmentRequest() throws PlayerManagementException, InterruptedException, IllegalGameStateException, GameException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);
        CityCardDTO cityCardDTO = mock(CityCardDTO.class);
        when(cityCardDTO.getId()).thenReturn(1);
        WaterTreatmentRequest waterTreatmentRequest = new WaterTreatmentRequest("testLobbyId", 1, 5, cityCardDTO);
        waterTreatmentRequest.setSession(session);

        ICard card = mock(ICard.class);
        when(card.getId()).thenReturn(1);
        when(playerManagement.getCard("testLobbyId", "testUser", 1)).thenReturn(card);

        IGame game = new Game(1, "testLobbyId");
        when(regionManagement.getGame("testLobbyId")).thenReturn(game);

        ILobby lobby = new Lobby("testLobbyId", "testLobbyName", List.of(user), user, 1);
        when(lobbyManagement.getLobby("testLobbyId")).thenReturn(lobby);

        postAndWait(waterTreatmentRequest);

        assertInstanceOf(BoardUpdateEvent.class, event);
        verify(regionManagement, times(1)).increaseWaterTreatmentsFromRegion("testLobbyId", 1, 5, card, user);
    }

    @Test
    void testOnSendWaterTreatmentRequest_IllegalGameState() throws PlayerManagementException, InterruptedException, IllegalGameStateException, GameException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);
        CityCardDTO cityCardDTO = mock(CityCardDTO.class);
        when(cityCardDTO.getId()).thenReturn(1);
        WaterTreatmentRequest waterTreatmentRequest = new WaterTreatmentRequest("testLobbyId", 1, 5, cityCardDTO);
        waterTreatmentRequest.setSession(session);

        ICard card = mock(ICard.class);
        when(card.getId()).thenReturn(1);
        when(playerManagement.getCard("testLobbyId", "testUser", 1)).thenReturn(card);

        doThrow(new IllegalGameStateException("")).when(regionManagement)
                                                  .increaseWaterTreatmentsFromRegion("testLobbyId", 1, 5, card, user);

        postAndWait(waterTreatmentRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    @Test
    void testOnSendWaterTreatmentRequest_CardCouldNotBeDiscarded() throws PlayerManagementException, InterruptedException, IllegalGameStateException, GameException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);
        CityCardDTO cityCardDTO = mock(CityCardDTO.class);
        when(cityCardDTO.getId()).thenReturn(1);
        WaterTreatmentRequest waterTreatmentRequest = new WaterTreatmentRequest("testLobbyId", 1, 5, cityCardDTO);
        waterTreatmentRequest.setSession(session);

        ICard card = mock(ICard.class);
        when(card.getId()).thenReturn(1);
        when(playerManagement.getCard("testLobbyId", "testUser", 1)).thenReturn(card);

        doThrow(new GameException("")).when(regionManagement)
                                      .increaseWaterTreatmentsFromRegion("testLobbyId", 1, 5, card, user);

        postAndWait(waterTreatmentRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    @Test
    void testOnSendWaterTreatmentRequest_CouldNotGetCard() throws PlayerManagementException, InterruptedException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);
        CityCardDTO cityCardDTO = mock(CityCardDTO.class);
        when(cityCardDTO.getId()).thenReturn(1);
        WaterTreatmentRequest waterTreatmentRequest = new WaterTreatmentRequest("testLobbyId", 1, 5, cityCardDTO);
        waterTreatmentRequest.setSession(session);

        when(playerManagement.getCard("testLobbyId", "testUser", 1)).thenThrow(PlayerManagementException.class);

        postAndWait(waterTreatmentRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    @Test
    void testOnTreatWaterEvent() throws InterruptedException {
        TreatWaterEvent treatWaterEvent = new TreatWaterEvent("lobby1", "user1");
        IGame game = mock(IGame.class);
        IUser user = new User("user1", "password");
        IPlayer player = new Player(user);
        Session session = UUIDSession.create(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        when(regionManagement.getGame("lobby1")).thenReturn(game);
        when(game.getPlayer("user1")).thenReturn(player);

        postAndWait(treatWaterEvent);

        assertInstanceOf(TreatWaterEventResponse.class, event);
    }

    @Test
    void testOnTreatWaterEvent_MissingSession() {
        TreatWaterEvent event = new TreatWaterEvent("lobby1", "user1");
        IGame game = mock(IGame.class);
        when(regionManagement.getGame("lobby1")).thenReturn(game);
        IPlayer player = mock(IPlayer.class);
        when(game.getPlayer("user1")).thenReturn(player);
        when(player.getUser()).thenReturn(mock(IUser.class));

        assertThrows(SessionNotFoundException.class, () -> regionService.onTreatWaterEvent(event));
    }

    @Test
    void testOnWaterTreatmentEventRequest() {
        WaterTreatmentEventRequest request = mock(WaterTreatmentEventRequest.class);
        IGame game = mock(IGame.class);
        IUserDTO userDTO = mock(IUserDTO.class);
        Session session = mock(Session.class);
        ILobby lobby = mock(ILobby.class);
        ICity city = mock(ICity.class);
        RegionRepository regionRepository = mock(RegionRepository.class);
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        PlagueRepository plagueRepository = mock(PlagueRepository.class);
        CityRepository cityRepository = mock(CityRepository.class);

        when(game.getCityRepository()).thenReturn(cityRepository);
        when(cityRepository.getCities()).thenReturn(List.of(city));
        when(game.getConnectionRepository()).thenReturn(connectionRepository);
        when(game.getRegionRepository()).thenReturn(regionRepository);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(userDTO.getUsername()).thenReturn("user1");
        when(userDTO.getPassword()).thenReturn("password");
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(userDTO);
        when(request.getLobbyId()).thenReturn("lobby1");
        when(regionManagement.getGame("lobby1")).thenReturn(game);
        when(lobbyManagement.getLobby("lobby1")).thenReturn(lobby);
        when(authenticationService.getSession(UserMapper.toUser(userDTO))).thenReturn(Optional.of(session));

        when(game.getState()).thenReturn(mock(EventState.class));
        when(request.getAmount()).thenReturn(1);

        regionService.onWaterTreatmentEventRequest(request);

        verify(regionManagement).increaseWaterTreatment(anyInt(), eq(game), eq(1));
        verify(game).setState(any(PlaceExtraWaterTreatmentState.class));
    }

    @Test
    void testOnWaterTreatmentEventRequest_Dismissed() {
        WaterTreatmentEventRequest request = mock(WaterTreatmentEventRequest.class);
        IGame game = mock(IGame.class);
        IUserDTO userDTO = mock(IUserDTO.class);
        Session session = mock(Session.class);
        ILobby lobby = mock(ILobby.class);
        ICity city = mock(ICity.class);
        RegionRepository regionRepository = mock(RegionRepository.class);
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        PlagueRepository plagueRepository = mock(PlagueRepository.class);
        CityRepository cityRepository = mock(CityRepository.class);

        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        when(game.getCityRepository()).thenReturn(cityRepository);
        when(cityRepository.getCities()).thenReturn(List.of(city));
        when(game.getConnectionRepository()).thenReturn(connectionRepository);
        when(game.getRegionRepository()).thenReturn(regionRepository);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(userDTO);
        when(request.getLobbyId()).thenReturn("lobby1");
        when(regionManagement.getGame("lobby1")).thenReturn(game);
        when(lobbyManagement.getLobby("lobby1")).thenReturn(lobby);
        when(request.isDismissed()).thenReturn(true);

        regionService.onWaterTreatmentEventRequest(request);

        verify(game).setState(game.getPreviousState());
    }

    @Test
    void testOnWaterTreatmentEventRequest_NotDismissed() {
        WaterTreatmentEventRequest request = mock(WaterTreatmentEventRequest.class);
        IGame game = mock(IGame.class);
        IUserDTO userDTO = mock(IUserDTO.class);
        Session session = mock(Session.class);
        ILobby lobby = mock(ILobby.class);
        ICity city = mock(ICity.class);
        RegionRepository regionRepository = mock(RegionRepository.class);
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        PlagueRepository plagueRepository = mock(PlagueRepository.class);
        CityRepository cityRepository = mock(CityRepository.class);

        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        when(game.getCityRepository()).thenReturn(cityRepository);
        when(cityRepository.getCities()).thenReturn(List.of(city));
        when(game.getConnectionRepository()).thenReturn(connectionRepository);
        when(game.getRegionRepository()).thenReturn(regionRepository);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(request.getSession()).thenReturn(Optional.of(session));
        when(session.getUser()).thenReturn(userDTO);
        when(request.getLobbyId()).thenReturn("lobby1");
        when(regionManagement.getGame("lobby1")).thenReturn(game);
        when(lobbyManagement.getLobby("lobby1")).thenReturn(lobby);
        when(request.isDismissed()).thenReturn(false);
        when(request.getAmount()).thenReturn(2);

        regionService.onWaterTreatmentEventRequest(request);

        verify(regionManagement).increaseWaterTreatment(request.getRegionId(), game, request.getAmount());
        verify(game).setState(game.getPreviousState());
    }

    @Test
    void testOnWaterTreatmentEventRequest_MissingSession() {
        WaterTreatmentEventRequest request = new WaterTreatmentEventRequest("lobby1", 1, 1, false);

        assertThrows(SessionNotFoundException.class, () -> regionService.onWaterTreatmentEventRequest(request));
    }
}