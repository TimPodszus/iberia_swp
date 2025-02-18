package de.uol.swp.server.region;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.message.request.AvailableRegionsRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegionServiceTest extends EventBusBasedTest {

    @Mock
    private IRegionManagement regionManagement;

    @Mock
    private IPlayerManagement playerManagement;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    RegionService regionService = new RegionService(
            getBus(),
            regionManagement,
            playerManagement,
            gameManagement,
            lobbyManagement
    );

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

    @Test
    public void testOnSendAvailableRegionsRequest_UserUnknown() {
        AvailableRegionsRequest availableRegionsRequest = mock(AvailableRegionsRequest.class);
        Session session = mock(Session.class);

        when(availableRegionsRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        EventBusException exception = assertThrows(
                EventBusException.class, () -> {
                    postAndWait(availableRegionsRequest);
                }
        );

        Throwable cause = exception.getCause();
        assertInstanceOf(GameException.class, cause);
        assertEquals("User is unknown", cause.getMessage());
    }

    @Test
    public void testOnSendAvailableRegionsRequest_Success() throws InterruptedException {
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
    public void testOnWaterTreatmentRegionRequest_UserUnknown() {
        WaterTreatmentRegionRequest waterTreatmentRegionRequest = mock(WaterTreatmentRegionRequest.class);
        Session session = mock(Session.class);

        when(waterTreatmentRegionRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        EventBusException exception = assertThrows(
                EventBusException.class, () -> {
                    postAndWait(waterTreatmentRegionRequest);
                }
        );

        Throwable cause = exception.getCause();
        assertTrue(cause instanceof GameException);
        assertEquals("User is unknown", cause.getMessage());
    }

    @Test
    public void testOnWaterTreatmentRegionRequest_Success() throws InterruptedException {
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
    public void testOnSendWaterTreatmentRequest_UserUnknown() {
        WaterTreatmentRequest waterTreatmentRequest = mock(WaterTreatmentRequest.class);
        Session session = mock(Session.class);

        when(waterTreatmentRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        EventBusException exception = assertThrows(
                EventBusException.class, () -> {
                    postAndWait(waterTreatmentRequest);
                }
        );

        Throwable cause = exception.getCause();
        assertInstanceOf(GameException.class, cause);
        assertEquals("User is unknown", cause.getMessage());
    }

    @Test
    public void testOnSendWaterTreatmentRequest_Success() throws  PlayerManagementException, GameManagementException, InterruptedException {
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
        when(gameManagement.getGame(lobbyId)).thenReturn(game);
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
        verify(gameManagement, times(1)).getGame(lobbyId);
        verify(lobbyManagement, times(1)).getLobby(lobbyId);
    }
}