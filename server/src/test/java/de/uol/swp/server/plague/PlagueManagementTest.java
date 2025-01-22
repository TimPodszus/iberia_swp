package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.plague.data.Plague;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.region.data.Region;
import de.uol.swp.server.role.CountryDoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for PlagueManagement.
 */
class PlagueManagementTest {

    @Mock
    private Game game;
    @Mock
    private Player currentPlayer;
    @Mock
    private City currentCity;
    @Mock
    private PlagueRepository plagueRepository;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private Plague plague;
    @InjectMocks
    private PlagueManagement plagueManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        List<Card> playerCards = List.of(
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA)
        );

        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getCards()).thenReturn(playerCards);

        when(game.getRegionRepository()).thenReturn(regionRepository);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(plagueRepository.getPlagues()).thenReturn(List.of(plague));
        when(plague.getName()).thenReturn(PlagueName.CHOLERA);

        IGameState gameState = mock(IGameState.class);
        when(game.getState()).thenReturn(gameState);
    }

    /**
     * Tests that an exception is thrown when the plague to be researched is null.
     * Verifies that the correct error message is provided when a PlagueManagementException is thrown.
     */
    @Test
    void researchPlagueIsNullThrowsPlagueManagementException() {
        PlagueManagementException exception = assertThrows(
                PlagueManagementException.class,
                () -> plagueManagement.researchPlague(null, game)
        );
        assertEquals("The plague to be researched was not specified", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when the plague has already been researched.
     * Verifies that the correct error message is provided when a PlagueManagementException is thrown.
     */
    @Test
    void researchPlagueAlreadyResearchedThrowsPlagueManagementException() {
        when(plague.isResearched()).thenReturn(true);

        PlagueManagementException exception = assertThrows(
                PlagueManagementException.class,
                () -> plagueManagement.researchPlague(PlagueName.CHOLERA, game)
        );

        assertEquals("The plague is already researched", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when the player does not have enough cards to research the plague.
     * Verifies that the correct error message is provided when a PlagueManagementException is thrown.
     */
    @Test
    void notEnoughCardsToResearchPlagueThrowsPlagueManagementException() {
        when(currentPlayer.getCards()).thenReturn(new ArrayList<>());

        PlagueManagementException exception = assertThrows(
                PlagueManagementException.class,
                () -> plagueManagement.researchPlague(PlagueName.CHOLERA, game)
        );

        assertEquals("Player has not enough cards to research the plague", exception.getMessage());
    }

    /**
     * Tests that an exception is thrown when the current city does not have a hospital built.
     * Verifies that the correct error message is provided when a PlagueManagementException is thrown.
     */
    @Test
    void noHospitalInCurrentCityThrowsPlagueManagementException() {
        when(currentPlayer.getCurrentPosition()).thenReturn(currentCity);
        when(currentCity.isHospitalBuilt()).thenReturn(false);
        when(currentCity.getPlagueName()).thenReturn(PlagueName.CHOLERA);

        PlagueManagementException exception = assertThrows(
                PlagueManagementException.class,
                () -> plagueManagement.researchPlague(PlagueName.CHOLERA, game)
        );

        assertEquals("No suitable hospital in the current city to research the plague", exception.getMessage());
    }

    /**
     * Tests a valid scenario where the plague is successfully researched.
     * Verifies that the correct number of cards are discarded, the plague is marked as researched,
     * and the game state is updated accordingly.
     */
    @Test
    void researchPlagueSuccessful() throws PlagueManagementException {
        when(plague.isResearched()).thenReturn(false);

        when(currentPlayer.getCurrentPosition()).thenReturn(currentCity);
        when(currentCity.isHospitalBuilt()).thenReturn(true);
        when(currentCity.getPlagueName()).thenReturn(PlagueName.CHOLERA);

        List<Card> discardPile = new ArrayList<>();
        when(game.getPlayerCardDiscardPile()).thenReturn(discardPile);

        IGameState gameState = mock(IGameState.class);
        when(game.getState()).thenReturn(gameState);

        plagueManagement.researchPlague(PlagueName.CHOLERA, game);

        verify(currentPlayer, times(5)).discardCard(any(CityCard.class));
        assertEquals(5, discardPile.size());

        verify(plague).setResearched(true);
        verify(gameState).handleAction(game, currentPlayer);
    }

    /**
     * Helper method to mock a CityCard and associate it with a specific plague.
     *
     * @param plagueName the name of the plague to associate with the CityCard.
     * @return a mock CityCard associated with the specified plague.
     */
    private CityCard mockCityCard(PlagueName plagueName) {
        CityCard cityCard = mock(CityCard.class);
        City city = mock(City.class);

        when(city.getPlagueName()).thenReturn(plagueName);
        when(cityCard.getCity()).thenReturn(city);

        return cityCard;
    }

    @Test
    void treatPlagueHasNoPlagueInCityThrowsPlagueManagementException() {
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getCurrentPosition()).thenReturn(currentCity);
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(false);
        City secondCity = mock(City.class);
        PlagueName plagueToTreatInSecondCity = PlagueName.CHOLERA;
        PlagueManagementException exception = assertThrows(
                PlagueManagementException.class,
                () -> plagueManagement.treatPlague(PlagueName.CHOLERA, secondCity, plagueToTreatInSecondCity, game)
        );
        assertEquals("The selected plague is not present in the current city.", exception.getMessage());
    }
    @Test
    void treatPlagueNoCubesInCityThrowsPlagueManagementException() {
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getCurrentPosition()).thenReturn(currentCity);
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        City secondCity = mock(City.class);
        PlagueName plagueToTreatInSecondCity = PlagueName.CHOLERA;
        when(currentCity.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(0);
        PlagueManagementException exception = assertThrows(
                PlagueManagementException.class,
                () -> plagueManagement.treatPlague(PlagueName.CHOLERA, secondCity, plagueToTreatInSecondCity, game)
        );
        assertEquals("No plague cubes to remove for the selected plague.", exception.getMessage());
    }
    @Test
    void treatPlagueWithNullInputThrowsIllegalArgumentException() {
        City secondCity = mock(City.class);
        PlagueName plagueToTreatInSecondCity = PlagueName.CHOLERA;
        assertThrows(IllegalArgumentException.class,
                () -> plagueManagement.treatPlague(null, secondCity, plagueToTreatInSecondCity, game));
    }
    @Test
    void countryDoctorTreatsPlagueInAdjacentCitySuccessfully() throws PlagueManagementException {
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getCurrentPosition()).thenReturn(currentCity);
        when(currentPlayer.getRole()).thenReturn(new CountryDoctor());
        City secondCity = mock(City.class);
        Region currentRegion = mock(Region.class);
        when(game.getRegionRepository().getRegions()).thenReturn(List.of(currentRegion));
        when(currentRegion.getSurroundingCities()).thenReturn(List.of(currentCity));
        when(game.getRegionRepository().getCitiesInAdjacentRegions(currentRegion)).thenReturn(List.of(secondCity));
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(currentCity.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(2);
        when(secondCity.getPlagueCubes(PlagueName.TYPHUS)).thenReturn(2);
        plagueManagement.treatPlague(PlagueName.CHOLERA, secondCity, PlagueName.TYPHUS, game);
        verify(currentCity).removePlagueCubes(PlagueName.CHOLERA, 1);
        verify(secondCity).removePlagueCubes(PlagueName.TYPHUS, 1);
    }
}