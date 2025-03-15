package de.uol.swp.server.plague;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.plague.message.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.message.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.message.request.TreatPlagueRequest;
import de.uol.swp.common.plague.message.response.AvailablePlaguesResponse;
import de.uol.swp.common.plague.message.response.MigrationOverseasResponse;
import de.uol.swp.common.plague.message.response.TreatPlagueResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.cards.events.MigrationOverseasEvent;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.*;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.data.Infection;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.plague.management.PlagueNotFoundException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.role.CountryDoctor;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlagueServiceTest extends EventBusBasedTest {

    @InjectMocks
    private PlagueService plagueService;
    @Mock
    private IPlagueManagement plagueManagement;
    @Mock
    private ILobbyManagement lobbyManagement;
    @Mock
    private EventBus eventBus;

    private final IGame game = new Game(2, "lobby123");
    @Mock
    private IPlayer player;
    @Mock
    private ICity city;
    @Mock
    private ILobby lobby;
    @Mock
    private IUser user;
    @Mock
    private AvailablePlaguesRequest availablePlaguesRequest;
    @Mock
    private ResearchPlagueRequest researchPlagueRequest;
    @Mock
    private TreatPlagueRequest treatPlagueRequest;
    @Mock
    private AuthenticationService authenticationService;

    private final Session session = UUIDSession.create(user);
    @Mock
    private IGameState previousState;

    @Subscribe
    public void onMigrationOverseasResponse(MigrationOverseasResponse response) {
        super.event = response;
    }

    @Subscribe
    public void onTreatPlagueResponse(TreatPlagueResponse response) {
        super.event = response;
    }

    @Subscribe
    public void onTreatPlagueRequest(TreatPlagueRequest request) {
        plagueService.onTreatPlagueRequest(request);
    }

    @Subscribe
    public void onMigrationOverseasEvent(MigrationOverseasEvent event) {
        plagueService.onMigrationOverseasEvent(event);
    }

    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);

        GameStore.getInstance()
                 .addGame("lobby123", game);
        when(plagueManagement.getGame("lobby123")).thenReturn(game);

        Field authServiceField = AbstractService.class.getDeclaredField("authenticationService");
        authServiceField.setAccessible(true);
        authServiceField.set(plagueService, authenticationService);
        game.getPlayers().add(player);

        when(player.getUser()).thenReturn(user);
        when(player.getCurrentPosition()).thenReturn(city);
        IRole roleMock = mock(IRole.class);
        when(player.getRole()).thenReturn(mock(IRole.class));
        when(roleMock.getName()).thenReturn(RoleEnum.COUNTRY_DOCTOR);
        when(plagueManagement.getGame(anyString())).thenReturn(game);

        when(treatPlagueRequest.getLobbyId()).thenReturn("lobby123");
        when(treatPlagueRequest.getCityId()).thenReturn(1);
        when(treatPlagueRequest.getPlagueName()).thenReturn(PlagueName.CHOLERA);

        when(city.getName()).thenReturn(CityName.ALICANTE);
        when(lobbyManagement.getLobby("lobby123")).thenReturn(lobby);
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
    void testOnTreatPlagueRequest_Success() throws IllegalGameStateException, PlagueNotFoundException, InterruptedException {
        game.setState(new PlayerTurnState());
        when(player.getRole()).thenReturn(mock(CountryDoctor.class));
        when(player.getCurrentPosition()).thenReturn(game.getCityRepository().getCity(1));
        when(plagueManagement.getCitiesNearBy(game, city)).thenReturn(Collections.emptyList());

        postAndWait(new TreatPlagueRequest("lobby123", 1, PlagueName.CHOLERA));

        verify(plagueManagement).treatPlague(PlagueName.CHOLERA, player.getCurrentPosition(), game);
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
        game.setState(new PlayerTurnState());
        CountryDoctor countryDoctorMock = mock(CountryDoctor.class);
        when(player.getRole()).thenReturn(countryDoctorMock);
        ICity testCity = new City(1, PlagueName.CHOLERA, CityName.PORTO, -136, true);
        testCity.getInfections().add(new Infection(3, PlagueName.CHOLERA));
        when(player.getCurrentPosition()).thenReturn(testCity);
        List<ICity> citiesNearBy = List.of(testCity);
        when(plagueManagement.getCitiesNearBy(game, testCity)).thenReturn(citiesNearBy);

        plagueService.onTreatPlagueRequest(new TreatPlagueRequest("lobby123", 1, PlagueName.CHOLERA));

        assertInstanceOf(TreatExtraPlagueState.class, game.getState());
        verify(eventBus).post(argThat(response -> response instanceof TreatPlagueResponse && ((TreatPlagueResponse) response).isSuccess()));
    }

    @Test
    void testOnTreatPlagueRequest_EventState() {
        game.setState(new PlayerTurnState());
        game.setState(new TreatExtraPlagueState(4));
        ICity testCity = new City(1, PlagueName.CHOLERA, CityName.PORTO, -136, true);
        testCity.getInfections().add(new Infection(3, PlagueName.CHOLERA));
        when(player.getCurrentPosition()).thenReturn(testCity);
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class));
        when(plagueManagement.getCitesWithPlagues(game)).thenReturn(availableCities);

        plagueService.onTreatPlagueRequest(new TreatPlagueRequest("lobby123", 1, PlagueName.CHOLERA));

        assertInstanceOf(PlayerTurnState.class, game.getState());

        game.setState(new EventState(mock(EventCard.class)));
        when(plagueManagement.getCitesWithPlagues(game)).thenReturn(availableCities);

        plagueService.onTreatPlagueRequest(new TreatPlagueRequest("lobby123", 1, PlagueName.CHOLERA));

        assertInstanceOf(TreatExtraPlagueState.class, game.getState());
        verify(eventBus).post(argThat(response -> response instanceof TreatPlagueResponse && ((TreatPlagueResponse) response).isSuccess()));
    }

    @Test
    void testOnMigrationOverseasEvent() throws InterruptedException {
        MigrationOverseasEvent event = new MigrationOverseasEvent("lobby123", "username");
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));
        when(user.getUsername()).thenReturn("username");
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class));
        when(plagueManagement.getCitesWithPlagues(game)).thenReturn(availableCities);

        postAndWait(event);

        verify(plagueManagement, times(1)).getCitesWithPlagues(game);
    }

    @Test
    void testOnMigrationOverseasEvent_SessionNotFoundException() {
        MigrationOverseasEvent event = new MigrationOverseasEvent("lobby123", "username");
        when(authenticationService.getSession(user)).thenReturn(Optional.empty());
        when(user.getUsername()).thenReturn("username");

        assertThrows(SessionNotFoundException.class, () -> plagueService.onMigrationOverseasEvent(event));
    }
}
