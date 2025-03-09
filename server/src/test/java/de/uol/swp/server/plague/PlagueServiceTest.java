package de.uol.swp.server.plague;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.plague.response.AvailablePlaguesResponse;
import de.uol.swp.common.plague.response.TreatPlagueResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.TreatExtraPlagueState;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.plague.management.PlagueNotFoundException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.CountryDoctor;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.*;

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
    private ResearchPlagueRequest researchPlagueRequest;
    @Mock
    private TreatPlagueRequest treatPlagueRequest;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private Session session;
    @Mock
    private IGameState previousState;

    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);

        Field authServiceField = AbstractService.class.getDeclaredField("authenticationService");
        authServiceField.setAccessible(true);
        authServiceField.set(plagueService, authenticationService);

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
    void testOnResearchPlagueRequest() throws PlagueManagementException, IllegalGameStateException, GameException {
        when(researchPlagueRequest.getLobbyId()).thenReturn("lobby123");
        when(researchPlagueRequest.getSession()).thenReturn(Optional.of(session));
        when(plagueManagement.getGame(researchPlagueRequest.getLobbyId())).thenReturn(game);

        plagueService.onResearchPlagueRequest(researchPlagueRequest);
        verify(plagueManagement).researchPlague(any());
        verify(eventBus).post(any(BoardUpdateEvent.class));
    }

    @Test
    void testOnResearchPlagueRequest_PlagueManagementException() throws PlagueManagementException, IllegalGameStateException, GameException {
        when(researchPlagueRequest.getLobbyId()).thenReturn("lobby123");
        when(researchPlagueRequest.getSession()).thenReturn(Optional.of(session));
        when(plagueManagement.getGame(researchPlagueRequest.getLobbyId())).thenReturn(game);

        doThrow(new PlagueManagementException("Error while researching plague")).when(plagueManagement)
                                                                                .researchPlague(any());
        plagueService.onResearchPlagueRequest(researchPlagueRequest);
        verify(eventBus).post(argThat(response -> response instanceof StatusResponse && !((StatusResponse) response).isSuccess() && ((StatusResponse) response).getDescription()
                                                                                                                                                               .equals("Fehler beim Erforschen der Seuche")));
    }

    @Test
    void testOnResearchPlagueRequest_IllegalGameStateException() throws PlagueManagementException, IllegalGameStateException, GameException {
        when(researchPlagueRequest.getLobbyId()).thenReturn("lobby123");
        when(researchPlagueRequest.getSession()).thenReturn(Optional.of(session));
        when(plagueManagement.getGame(researchPlagueRequest.getLobbyId())).thenReturn(game);

        doThrow(new IllegalGameStateException("Game is in an illegal state")).when(plagueManagement)
                                                                             .researchPlague(any());
        plagueService.onResearchPlagueRequest(researchPlagueRequest);
        verify(eventBus).post(argThat(response -> response instanceof StatusResponse && !((StatusResponse) response).isSuccess() && ((StatusResponse) response).getDescription()
                                                                                                                                                               .equals("In dem Zustand des Spiels kann die Seuche nicht " + "erforscht werden.")));
    }

    @Test
    void testOnAvailablePlaguesRequest() {
        int cityId = 444;

        when(availablePlaguesRequest.getLobbyId()).thenReturn("lobby123");
        when(availablePlaguesRequest.getCityId()).thenReturn(cityId);
        List<IInfection> infections = new ArrayList<>();
        when(plagueManagement.getInfectionsInCity(game, cityId)).thenReturn(infections);

        plagueService.onAvailablePlaguesRequest(availablePlaguesRequest);
        verify(plagueManagement).getInfectionsInCity(game, cityId);
        verify(eventBus).post(any(AvailablePlaguesResponse.class));
    }

    @Test
    void testOnTreatPlagueRequest_Success() throws IllegalGameStateException, PlagueNotFoundException {
        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getRole()).thenReturn(mock(CountryDoctor.class));
        when(plagueManagement.getCitiesNearBy(game, city)).thenReturn(Collections.emptyList());

        plagueService.onTreatPlagueRequest(treatPlagueRequest);
        verify(plagueManagement).treatPlague(PlagueName.CHOLERA, city, game);
        verify(eventBus).post(any(BoardUpdateEvent.class));
    }

    @Test
    void testOnTreatPlagueRequest_IllegalGameStateException() throws IllegalGameStateException, PlagueNotFoundException {
        doThrow(new IllegalGameStateException("Invalid state")).when(plagueManagement)
                                                               .treatPlague(any(), any(), any());
        plagueService.onTreatPlagueRequest(treatPlagueRequest);
        verify(eventBus).post(any(StatusResponse.class));
    }

    @Test
    void testOnTreatPlagueRequest_PlagueNotFoundException() throws IllegalGameStateException, PlagueNotFoundException {
        doThrow(new PlagueNotFoundException("Plague not found")).when(plagueManagement)
                                                                .treatPlague(any(), any(), any());
        plagueService.onTreatPlagueRequest(treatPlagueRequest);
        verify(eventBus).post(any(StatusResponse.class));
    }

    @Test
    void testOnTreatPlagueRequest_CountryDoctor_TreatExtraPlagueState() {
        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getCurrentPlayer()).thenReturn(player);

        CountryDoctor countryDoctorMock = mock(CountryDoctor.class);
        when(player.getRole()).thenReturn(countryDoctorMock);

        List<ICity> citiesNearBy = List.of(mock(ICity.class));
        when(plagueManagement.getCitiesNearBy(game, city)).thenReturn(citiesNearBy);

        plagueService.onTreatPlagueRequest(treatPlagueRequest);
        verify(game).setState(argThat(state -> state instanceof TreatExtraPlagueState));
        verify(eventBus).post(argThat(response -> response instanceof TreatPlagueResponse && ((TreatPlagueResponse) response).isSuccess()));
    }

    @Test
    void testOnTreatPlagueRequest_ExitTreatExtraPlagueState() {
        when(game.getState()).thenReturn(new TreatExtraPlagueState());
        when(game.getPreviousState()).thenReturn(previousState);
        when(game.getCurrentPlayer()).thenReturn(player);

        plagueService.onTreatPlagueRequest(treatPlagueRequest);
        verify(game).setState(previousState);
    }


}
