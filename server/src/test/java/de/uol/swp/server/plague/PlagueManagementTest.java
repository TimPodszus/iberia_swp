package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.Plague;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.region.data.IRegion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for PlagueManagement.
 */
class PlagueManagementTest {

    @Mock
    IPlayerManagement playerManagement;
    @Mock
    PlayerTurnState playerTurnState;
    @Mock
    private Game game;
    @Mock
    private IPlayer currentPlayer;
    @Mock
    private ICity currentCity;
    @Mock
    private PlagueRepository plagueRepository;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private IPlague plague;
    @InjectMocks
    private PlagueManagement plagueManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        List<ICard> playerCards = List.of(mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA)
        );

        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getCards()).thenReturn(playerCards);
        when(currentPlayer.getCurrentPosition()).thenReturn(currentCity);

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
        PlagueManagementException exception = assertThrows(PlagueManagementException.class,
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

        PlagueManagementException exception = assertThrows(PlagueManagementException.class,
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

        PlagueManagementException exception = assertThrows(PlagueManagementException.class,
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

        PlagueManagementException exception = assertThrows(PlagueManagementException.class,
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

        plagueManagement.researchPlague(PlagueName.CHOLERA, game);

        verify(plague).setResearched(true);
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

    /**
     * Tests the scenario where all plagues have been researched.
     * Ensures that the game transitions to the EndGameState when all plagues are marked as researched.
     */
    @Test
    void testAllPlaguesResearched_AllResearched() {
        IPlague plague1 = mock(Plague.class);
        IPlague plague2 = mock(Plague.class);
        when(plagueRepository.getPlagues()).thenReturn(List.of(plague1, plague2));
        when(plague1.isResearched()).thenReturn(true);
        when(plague2.isResearched()).thenReturn(true);

        plagueManagement.allPlaguesResearched(game);

        verify(game).setState(any(EndGameState.class));
    }

    /**
     * Tests the scenario where not all plagues have been researched.
     * Ensures that the game does not transition to the EndGameState
     * if at least one plague remains unresearched.
     */
    @Test
    void testAllPlaguesResearched_NotAllResearched() {
        IPlague plague1 = mock(Plague.class);
        IPlague plague2 = mock(Plague.class);
        when(plagueRepository.getPlagues()).thenReturn(List.of(plague1, plague2));
        when(plague1.isResearched()).thenReturn(true);
        when(plague2.isResearched()).thenReturn(false);

        plagueManagement.allPlaguesResearched(game);

        verify(game, never()).setState(any(EndGameState.class));
    }

    /**
     * Tests the scenario where an attempt is made to treat a plague that is not present in the city.
     * Ensures that a {@link PlagueManagementException} is thrown with the expected message.
     */
    @Test
    void testTreatPlague_PlagueNotInCity_ShouldThrowException() {
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(false);
        when(game.getState()).thenReturn(playerTurnState);

        PlagueManagementException exception = assertThrows(PlagueManagementException.class, () ->
                plagueManagement.treatPlague(PlagueName.CHOLERA, currentCity, game, false)
        );

        assertEquals("The selected plague is not present in the city:CHOLERA", exception.getMessage());
    }

    /**
     * Tests the scenario where an attempt is made to treat a plague, but no plague cubes are present in the city.
     * Ensures that a {@link PlagueManagementException} is thrown with the expected message.
     */
    @Test
    void testTreatPlague_NoPlagueCubes_ShouldThrowException() {
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(currentCity.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(0);
        when(game.getState()).thenReturn(playerTurnState);


        PlagueManagementException exception = assertThrows(PlagueManagementException.class, () ->
                plagueManagement.treatPlague(PlagueName.CHOLERA, currentCity, game, false)
        );

        assertEquals("No plague cubes to remove for the selected plague.", exception.getMessage());
    }


    @Test
    void testTreatPlague_SuccessfulTreatment_ShouldReducePlagueCubes() throws PlagueManagementException {
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(currentCity.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(2);
        when(game.getState()).thenReturn(playerTurnState);

        plagueManagement.treatPlague(PlagueName.CHOLERA, currentCity, game, false);

        verify(currentCity, times(1)).removePlagueCubes(PlagueName.CHOLERA, 1);
        verify(playerTurnState, times(1)).reduceActionsRemaining(game);
    }

    /**
     * Tests a successful plague treatment scenario.
     * Ensures that if the city has the selected plague and at least one plague cube,
     * the number of plague cubes is reduced by one and the player's action count is updated.
     */
    @Test
    void testTreatPlague_CountryDoctor_ShouldNotReduceActions() throws PlagueManagementException {
        when(currentCity.hasPlague(PlagueName.CHOLERA)).thenReturn(true);
        when(currentCity.getPlagueCubes(PlagueName.CHOLERA)).thenReturn(2);
        when(game.getState()).thenReturn(playerTurnState);

        plagueManagement.treatPlague(PlagueName.CHOLERA, currentCity, game, true);

        verify(currentCity, times(1)).removePlagueCubes(PlagueName.CHOLERA, 1);
        verify(playerTurnState, never()).reduceActionsRemaining(game);
    }

    /**
     * Tests the scenario where null inputs are provided to the treatPlague method.
     * Verifies that an IllegalArgumentException is thrown when either the plague or the city is null,
     * ensuring proper validation of inputs before proceeding with plague treatment.
     */
    @Test
    void testTreatPlague_NullInputs_ShouldThrowIllegalArgumentException() {
        when(game.getState()).thenReturn(playerTurnState);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                plagueManagement.treatPlague(null, currentCity, game, false)
        );
        assertEquals("Invalid input: plague or city cannot be null.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                plagueManagement.treatPlague(PlagueName.CHOLERA, null, game, false)
        );
        assertEquals("Invalid input: plague or city cannot be null.", exception.getMessage());

    }

    /**
     * Tests the scenario where infections in a city are retrieved.
     * Verifies that the correct list of infections is returned when calling the getInfectionsInCity method,
     * and ensures that the getInfections method of the city is called once to fetch the infections.
     */
    @Test
    void testGetInfectionsInCity_ShouldReturnInfections() {
        IInfection infection1 = mock(IInfection.class);
        IInfection infection2 = mock(IInfection.class);
        List<IInfection> expectedInfections = List.of(infection1, infection2);

        when(currentCity.getInfections()).thenReturn(expectedInfections);

        List<IInfection> result = plagueManagement.getInfectionsInCity(game);

        assertEquals(expectedInfections, result);
        verify(currentCity, times(1)).getInfections();
    }

    /**
     * Tests the scenario where there are no regions in the game.
     * Verifies that an empty list is returned when there are no regions in the game.
     */
    @Test
    void testGetCitiesNearBy_NoRegions() {
        when(game.getRegionRepository().getRegions()).thenReturn(new ArrayList<>());

        List<ICity> result = plagueManagement.getCitiesNearBy(game, currentCity);

        assertTrue(result.isEmpty(), "The result should be an empty list when no regions are present.");
    }

    /**
     * Tests the scenario where a city is in one region and adjacent regions have cities.
     * Verifies that the cities from adjacent regions are returned correctly.
     */
    @Test
    void testGetCitiesNearBy_CityInRegionWithAdjacentCities() {
        IRegion region1 = mock(IRegion.class);
        IRegion region2 = mock(IRegion.class);
        ICity adjacentCity = mock(ICity.class);
        ICity cityInRegion = mock(ICity.class);
        List<IRegion> allRegions = List.of(region1, region2);

        when(game.getRegionRepository().getRegions()).thenReturn(allRegions);
        when(region1.getSurroundingCities()).thenReturn(List.of(cityInRegion));
        when(region2.getSurroundingCities()).thenReturn(new ArrayList<>());
        when(game.getRegionRepository().getCitiesInAdjacentRegions(region1)).thenReturn(List.of(adjacentCity));

        List<ICity> result = plagueManagement.getCitiesNearBy(game, cityInRegion);

        assertEquals(1, result.size(), "There should be one adjacent city.");
        assertTrue(result.contains(adjacentCity), "The adjacent city should be included in the result.");
    }
}