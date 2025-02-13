package de.uol.swp.server.region;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.request.AvailableRegionsRequest;
import de.uol.swp.common.region.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.request.WaterTreatmentRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.region.management.RegionManagement;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegionServiceTest {

    @Mock
    private EventBus bus;
    @Mock
    private RegionManagement regionManagement;
    @Mock
    private PlayerManagement playerManagement;
    @Mock
    private GameManagement gameManagement;
    @Mock
    private LobbyManagement lobbyManagement;
    @Mock
    private AvailableRegionsRequest availableRegionsRequest;
    @Mock
    private WaterTreatmentRegionRequest waterTreatmentRegionRequest;
    @Mock
    private WaterTreatmentRequest waterTreatmentRequest;
    @Mock
    private Session session;
    @Mock
    private IUserDTO user;
    @Mock
    private IGame game;
    @Mock
    private ICard card;
    @Mock
    private ILobby lobby;
    @Mock
    private List<CityCardDTO> cityCards;
    @Mock
    private Set<IRegionDTO> regions;
    @Mock
    private CityRepository cityRepository;
    @InjectMocks
    private RegionService regionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnSendAvailableRegionsRequest_UserUnknown() {
        when(availableRegionsRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        assertThrows(GameException.class, () -> {
            regionService.onSendAvailableRegionsRequest(availableRegionsRequest);
        });
    }

    @Test
    void testOnSendAvailableRegionsRequest_Success() throws GameException {
        String lobbyId = "testLobbyId";
        when(availableRegionsRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(user);
        when(availableRegionsRequest.getLobbyId()).thenReturn(lobbyId);
        when(regionManagement.getGame(lobbyId)).thenReturn(game);
        when(regionManagement.getAvailableRegions(user, game)).thenReturn(regions);

        regionService.onSendAvailableRegionsRequest(availableRegionsRequest);

        verify(regionManagement, times(1)).getAvailableRegions(user, game);
    }

    @Test
    void testOnWaterTreatmentRegionRequest_UserUnknown() {
        when(waterTreatmentRegionRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        assertThrows(GameException.class, () -> {
            regionService.onWaterTreatmentRegionRequest(waterTreatmentRegionRequest);
        });
    }

    @Test
    void testOnWaterTreatmentRegionRequest_Success() throws GameException {
        String lobbyId = "testLobbyId";
        when(waterTreatmentRegionRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(user);
        when(waterTreatmentRegionRequest.getLobbyId()).thenReturn(lobbyId);
        when(regionManagement.getGame(lobbyId)).thenReturn(game);
        when(regionManagement.getPossibleCityCardsToDiscard(user, waterTreatmentRegionRequest.getRegionId(), game)).thenReturn(cityCards);

        regionService.onWaterTreatmentRegionRequest(waterTreatmentRegionRequest);

        verify(regionManagement, times(1)).getPossibleCityCardsToDiscard(user, waterTreatmentRegionRequest.getRegionId(), game);
    }

    @Test
    void testOnSendWaterTreatmentRequest_UserUnknown() {
        when(waterTreatmentRequest.getSession()).thenReturn(java.util.Optional.of(session));
        when(session.getUser()).thenReturn(null);

        assertThrows(GameException.class, () -> {
            regionService.onSendWaterTreatmentRequest(waterTreatmentRequest);
        });
    }
}