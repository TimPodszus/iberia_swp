package de.uol.swp.server.plague;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.infection.InfectionDTO;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.request.AvailableCitiesToTreatRequest;
import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.plague.response.AvailableCitiesToTreatResponse;
import de.uol.swp.common.plague.response.AvailablePlaguesResponse;
import de.uol.swp.common.plague.response.TreatPlagueResponse;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.IRole;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlagueServiceTest {

    @InjectMocks
    private PlagueService plagueService;
    @Mock
    private IPlagueManagement plagueManagement;
    @Mock
    private EventBus eventBus;
    @Mock
    private Game game;
    @Mock
    private IPlayer player;
    @Mock
    private ICity city;
    @Mock
    private RegionRepository regionRepository;

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
    }

    @Test
    void testOnResearchPlagueRequest_Success() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doNothing().when(plagueManagement).researchPlague(PlagueName.CHOLERA, game);

        plagueService.onResearchPlagueRequest(request, game);

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, times(1)).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnResearchPlagueRequest_Exception() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doThrow(new PlagueManagementException("Error")).when(plagueManagement).researchPlague(PlagueName.CHOLERA, game);

        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request, game));

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnAvailablePlaguesRequest() {
        AvailablePlaguesRequest request = mock(AvailablePlaguesRequest.class);
        when(request.getLobbyId()).thenReturn("lobby123");

        plagueService.onAvailablePlaguesRequest(request);

        verify(plagueManagement).getGame("lobby123");
        verify(plagueManagement).getInfectionsInCity(game);

        verify(eventBus).post(any(AvailablePlaguesResponse.class));
    }

    @Test
    void testOnTreatPlagueRequest() throws PlagueManagementException {
        TreatPlagueRequest request = mock(TreatPlagueRequest.class);
        when(request.getLobbyId()).thenReturn("lobby123");
        when(request.getCityId()).thenReturn(32);
        when(request.getPlagueName()).thenReturn(PlagueName.CHOLERA);
        when(request.isCountryDoctor()).thenReturn(false);

        when(plagueManagement.getGame("lobby123")).thenReturn(game);
        when(game.getCityRepository()).thenReturn(mock(CityRepository.class));
        when(game.getCityRepository().getCity(32)).thenReturn(city);

        doNothing().when(plagueManagement).treatPlague(PlagueName.CHOLERA, city, game, false);

        plagueService.onTreatPlagueRequest(request);

        verify(plagueManagement).getGame("lobby123");
        verify(game.getCityRepository()).getCity(32);
        verify(plagueManagement).treatPlague(PlagueName.CHOLERA, city, game, false);

        verify(eventBus).post(any(TreatPlagueResponse.class));
    }

    @Test
    void testOnAvailableCitiesToTreatRequest() {
        AvailableCitiesToTreatRequest request = mock(AvailableCitiesToTreatRequest.class);
        when(request.getLobbyId()).thenReturn("lobby123");

        ICity city1 = mock(ICity.class);
        ICity city2 = mock(ICity.class);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);

        List<ICity> nearbyCities = List.of(city1, city2);
        when(plagueManagement.getCitiesNearBy(game, city)).thenReturn(nearbyCities);

        IInfectionDTO iInfectionDTO = new InfectionDTO(2, PlagueName.CHOLERA);

        ICityDTO cityDTO1 = new CityDTO(1, PlagueName.CHOLERA, CityName.A_CORUNA, 122, false, false, List.of(iInfectionDTO));
        when(CityMapper.toDTOList(nearbyCities)).thenReturn(List.of(cityDTO1));

        plagueService.onAvailableCitiesToTreatRequest(request);

        verify(plagueManagement).getGame("lobby123");
        verify(plagueManagement).getCitiesNearBy(game, city);

        verify(eventBus).post(any(AvailableCitiesToTreatResponse.class));

        AvailableCitiesToTreatResponse response = new AvailableCitiesToTreatResponse("lobby123", true, List.of(cityDTO1));  // Argument-Captor Methode
        assertNotNull(response);
        assertEquals(2, response.getAvailableCities().size());
    }

}
