package de.uol.swp.server.region;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.message.request.AvailableRegionsRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentEventRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.events.TreatWaterEvent;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlaceExtraWaterTreatmentState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.EventBusBasedTest;
import org.greenrobot.eventbus.EventBusException;
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
    public void onTreatWaterEvent(TreatWaterEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onWaterTreatmentEventRequest(WaterTreatmentRequest request) {
        super.handleEvent(request);
    }

    @Test
    void testOnSendAvailableRegionsRequest_UserUnknown() {
        AvailableRegionsRequest availableRegionsRequest = mock(AvailableRegionsRequest.class);
        Session session = mock(Session.class);

        when(availableRegionsRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        EventBusException exception = assertThrows(EventBusException.class, () -> postAndWait(availableRegionsRequest));

        Throwable cause = exception.getCause();
        assertInstanceOf(GameException.class, cause);
        assertEquals("User is unknown", cause.getMessage());
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
        WaterTreatmentRegionRequest waterTreatmentRegionRequest = mock(WaterTreatmentRegionRequest.class);
        Session session = mock(Session.class);

        when(waterTreatmentRegionRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        EventBusException exception = assertThrows(
                EventBusException.class,
                () -> postAndWait(waterTreatmentRegionRequest)
        );

        Throwable cause = exception.getCause();
        assertInstanceOf(GameException.class, cause);
        assertEquals("User is unknown", cause.getMessage());
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
        WaterTreatmentRequest waterTreatmentRequest = mock(WaterTreatmentRequest.class);
        Session session = mock(Session.class);

        when(waterTreatmentRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        EventBusException exception = assertThrows(EventBusException.class, () -> postAndWait(waterTreatmentRequest));

        Throwable cause = exception.getCause();
        assertInstanceOf(GameException.class, cause);
        assertEquals("User is unknown", cause.getMessage());
    }

    @Test
    void testOnSendWaterTreatmentRequest_Success() throws PlayerManagementException, GameManagementException, InterruptedException, GameException {
        WaterTreatmentRequest waterTreatmentRequest = mock(WaterTreatmentRequest.class);
        Session session = mock(Session.class);
        IUserDTO userDTO = mock(IUserDTO.class);
        IUser user = mock(IUser.class);
        IGame game = mock(IGame.class);
        ICard card = mock(ICard.class);
        ILobby lobby = mock(ILobby.class);
        CityRepository cityRepository = mock(CityRepository.class);
        ICity city = mock(ICity.class);
        RegionRepository regionRepository = mock(RegionRepository.class);
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        PlagueRepository plagueRepository = mock(PlagueRepository.class);
        PlayerTurnState playerTurnState = mock(PlayerTurnState.class);

        String lobbyId = "testLobbyId";
        int regionId = 1;
        int amount = 5;
        String username = "testUser";
        CityCardDTO cityCard = mock(CityCardDTO.class);

        when(waterTreatmentRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(userDTO);
        when(userDTO.getUsername()).thenReturn(username);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));
        when(userDTO.getPassword()).thenReturn("testPassword");
        when(waterTreatmentRequest.getLobbyId()).thenReturn(lobbyId);
        when(waterTreatmentRequest.getRegionId()).thenReturn(regionId);
        when(waterTreatmentRequest.getAmount()).thenReturn(amount);
        when(waterTreatmentRequest.getCard()).thenReturn(cityCard);
        when(playerManagement.getCard(lobbyId, username, cityCard.getId())).thenReturn(card);
        when(regionManagement.getGame(lobbyId)).thenReturn(game);
        when(lobbyManagement.getLobby(lobbyId)).thenReturn(lobby);
        when(game.getCityRepository()).thenReturn(cityRepository);
        when(cityRepository.getCities()).thenReturn(List.of(city));
        when(game.getConnectionRepository()).thenReturn(connectionRepository);
        when(game.getRegionRepository()).thenReturn(regionRepository);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(game.getState()).thenReturn(playerTurnState);

        postAndWait(waterTreatmentRequest);

        verify(regionManagement, times(1)).increaseWaterTreatmentsFromRegion(
                lobbyId,
                regionId,
                amount,
                card,
                UserMapper.toUser(userDTO)
        );
        verify(regionManagement, times(1)).getGame(lobbyId);
        verify(lobbyManagement, times(1)).getLobby(lobbyId);
    }

    @Test
    void testOnTreatWaterEvent() {
        TreatWaterEvent event = new TreatWaterEvent("lobby1", "user1");
        IGame game = mock(IGame.class);
        IPlayer player = mock(IPlayer.class);
        IUser user = mock(IUser.class);
        Session session = mock(Session.class);

        when(regionManagement.getGame("lobby1")).thenReturn(game);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("user1");
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        regionService.onTreatWaterEvent(event);

        verify(regionManagement).getGame("lobby1");
        verify(authenticationService).getSession(user);
    }

    @Test
    void testOnWaterTreatmentEventRequest() throws GameException {
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
    void testOnWaterTreatmentEventRequest_Dismissed() throws GameException {
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
    void testOnWaterTreatmentEventRequest_NotDismissed() throws GameException {
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
}