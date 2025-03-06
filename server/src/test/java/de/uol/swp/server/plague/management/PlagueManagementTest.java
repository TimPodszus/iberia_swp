package de.uol.swp.server.plague.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.TreatExtraPlagueState;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.region.data.IRegion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlagueManagementTest {

    @InjectMocks
    private PlagueManagement plagueManagement;
    @Mock
    private IGame game;
    @Mock
    private IPlayer player;
    @Mock
    private ICity city;
    @Mock
    private IPlague plague;
    @Mock
    private PlagueRepository plagueRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
    }

    @Test
    void testTreatPlague() throws IllegalGameStateException, PlagueNotFoundException {
        when(city.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(city.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(1);
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        when(plagueRepository.getPlagueByName(PlagueName.CHOLERA)).thenReturn(plague);

        plagueManagement.treatPlague(PlagueName.CHOLERA, city, game);

        verify(city).removePlagueCubes(PlagueName.CHOLERA, 1);
        verify(game.getPlagueRepository()
                   .getPlagueByName(PlagueName.CHOLERA)).increaseCubes(1);
    }

    @Test
    void testTreatPlagueInvalidState() {
        when(city.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(city.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(1);
        when(game.getState()).thenReturn(mock(EndGameState.class));

        assertThrows(
                IllegalGameStateException.class,
                () -> plagueManagement.treatPlague(PlagueName.CHOLERA, city, game)
        );
    }


    @Test
    void testTreatPlagueNotPresent() {
        when(city.hasPlague(PlagueName.CHOLERA)).thenReturn(false);

        assertThrows(
                PlagueNotFoundException.class,
                () -> plagueManagement.treatPlague(PlagueName.CHOLERA, city, game)
        );
    }

    @Test
    void testTreatPlagueTreatExtraPlagueState() throws IllegalGameStateException, PlagueNotFoundException {
        TreatExtraPlagueState treatExtraPlagueState = mock(TreatExtraPlagueState.class);
        when(city.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(city.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(1);
        when(game.getState()).thenReturn(treatExtraPlagueState);
        when(plagueRepository.getPlagueByName(PlagueName.CHOLERA)).thenReturn(plague);

        plagueManagement.treatPlague(PlagueName.CHOLERA, city, game);

        verify(city).removePlagueCubes(PlagueName.CHOLERA, 1);
        verify(game.getPlagueRepository()
                   .getPlagueByName(PlagueName.CHOLERA)).increaseCubes(1);
    }

    @Test
    void testGetInfectionsInCity() {
        IInfection infection = mock(IInfection.class);
        when(infection.getSeverity()).thenReturn(2);
        when(city.getInfections()).thenReturn(List.of(infection));
        when(game.getCityRepository()).thenReturn(mock(CityRepository.class));
        when(game.getCityRepository()
                 .getCity(anyInt())).thenReturn(city);

        List<IInfection> infections = plagueManagement.getInfectionsInCity(game, 1);

        assertEquals(1, infections.size());
        assertEquals(infection, infections.get(0));
    }

    @Test
    void testGetCitiesNearBy() {
        IRegion region = mock(IRegion.class);
        ICity nearbyCity = mock(ICity.class);
        IInfection infection = mock(IInfection.class);
        when(infection.getSeverity()).thenReturn(2);
        when(nearbyCity.getInfections()).thenReturn(List.of(infection));
        when(region.getSurroundingCities()).thenReturn(List.of(city));
        when(game.getRegionRepository()).thenReturn(mock(RegionRepository.class));
        when(game.getRegionRepository()
                 .getRegions()).thenReturn(List.of(region));
        when(game.getRegionRepository()
                 .getCitiesInAdjacentRegions(region)).thenReturn(List.of(nearbyCity));

        List<ICity> cities = plagueManagement.getCitiesNearBy(game, city);

        assertEquals(1, cities.size());
        assertEquals(nearbyCity, cities.get(0));
    }

    @Test
    void testCanResearchPlagueSuccess() {
        when(city.isHospitalBuilt()).thenReturn(true);
        when(city.getPlagueName()).thenReturn(PlagueName.CHOLERA);

        List<ICard> cards = new ArrayList<>();
        cards.add(new CityCard(1, "Porto", new City(1, PlagueName.CHOLERA, CityName.PORTO, -136, true)));
        cards.add(new CityCard(2, "Coimbra",new City(2, PlagueName.CHOLERA, CityName.COIMBRA, -45, false)));
        cards.add(new CityCard(3, "Lisboa", new City(3, PlagueName.CHOLERA, CityName.LISBOA, -1000, true)));
        cards.add(new CityCard(4, "Albufeira", new City(4, PlagueName.CHOLERA, CityName.ALBUFEIRA, 750, true)));
        cards.add(new CityCard(5, "Evora", new City(5, PlagueName.CHOLERA, CityName.EVORA, -59, false)));
        cards.add(new CityCard(6, "Caceres", new City(6, PlagueName.CHOLERA, CityName.CACERES, -34, false)));

        when(game.getCurrentPlayer().getCards()).thenReturn(cards);
        when(game.getCurrentPlayer().getCurrentPosition().getPlagueName()).thenReturn(PlagueName.CHOLERA);
        boolean result = plagueManagement.canResearchPlague(game);

        assertTrue(result);
    }

    @Test
    void testCantResearchPlague() {
        when(city.isHospitalBuilt()).thenReturn(true);
        when(city.getPlagueName()).thenReturn(null);

        List<ICard> cards = new ArrayList<>();
        when(game.getCurrentPlayer().getCards()).thenReturn(cards);
        when(game.getCurrentPlayer().getCurrentPosition().getPlagueName()).thenReturn(PlagueName.CHOLERA);
        boolean result = plagueManagement.canResearchPlague(game);

        assertFalse(result);
    }


}