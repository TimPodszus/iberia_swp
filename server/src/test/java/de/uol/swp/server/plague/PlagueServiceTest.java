package de.uol.swp.server.plague;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.plague.response.AvailablePlaguesResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PlagueServiceTest {

    @InjectMocks
    private PlagueService plagueService;
    @Mock
    private IPlagueManagement plagueManagement;
    @Mock
    private ILobbyManagement lobbyManagement;
    @Mock
    private EventBus eventBus;
    @Mock
    private IGame game;
    @Mock
    private IPlayer player;
    @Mock
    private ICity city;
    @Mock
    private ILobby lobby;
    @Mock
    private IUser user;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private ConnectionRepository connectionRepository;
    @Mock
    private PlagueRepository plagueRepository;
    @Mock
    private AvailablePlaguesRequest availablePlaguesRequest;
    @Mock
    private TreatPlagueRequest treatPlagueRequest;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private Session session;
    @Mock
    private IGameState previousState;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);
        when(game.getRegionRepository()).thenReturn(regionRepository);

        IRole roleMock = mock(IRole.class);
        when(player.getRole()).thenReturn(roleMock);
        when(roleMock.getName()).thenReturn(RoleEnum.COUNTRY_DOCTOR);
        when(plagueManagement.getGame(anyString())).thenReturn(game);

        when(treatPlagueRequest.getLobbyId()).thenReturn("lobby123");
        when(treatPlagueRequest.getCityId()).thenReturn(1);
        when(treatPlagueRequest.getPlagueName()).thenReturn(PlagueName.CHOLERA);

        when(game.getCityRepository()).thenReturn(mock(CityRepository.class));
        when(game.getCityRepository()
                 .getCity(1)).thenReturn(city);
        when(city.getName()).thenReturn(CityName.ALICANTE);
        when(lobbyManagement.getLobby("lobby123")).thenReturn(lobby);
        when(game.getConnectionRepository()).thenReturn(connectionRepository);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getPreviousState()).thenReturn(previousState);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));
    }

    @Test
    void testOnResearchPlagueRequest_Success() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doNothing().when(plagueManagement)
                   .researchPlague(PlagueName.CHOLERA, game);

        plagueService.onResearchPlagueRequest(request, game);

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, times(1)).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnResearchPlagueRequest_Exception() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doThrow(new PlagueManagementException("Error")).when(plagueManagement)
                                                       .researchPlague(PlagueName.CHOLERA, game);

        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request, game));

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnAvailablePlaguesRequest() {
        String lobbyId = "lobby123";
        int cityId = 444;

        when(availablePlaguesRequest.getLobbyId()).thenReturn(lobbyId);
        when(availablePlaguesRequest.getCityId()).thenReturn(cityId);
        List<IInfection> infections = new ArrayList<>();
        when(plagueManagement.getInfectionsInCity(game, cityId)).thenReturn(infections);

        plagueService.onAvailablePlaguesRequest(availablePlaguesRequest);

        verify(plagueManagement).getInfectionsInCity(game, cityId);
        verify(eventBus).post(any(AvailablePlaguesResponse.class));

    }
}
