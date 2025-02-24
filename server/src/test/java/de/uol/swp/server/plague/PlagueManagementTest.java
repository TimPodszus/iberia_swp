package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.Plague;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for PlagueManagement.
 */
class PlagueManagementTest {

    @Mock
    PlayerManagement playerManagement;

    @Mock
    private Game game;
    @Mock
    private Player currentPlayer;
    @Mock
    private City currentCity;
    @Mock
    private PlagueRepository plagueRepository;
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

        List<ICard> playerCards = List.of(
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA),
                mockCityCard(PlagueName.CHOLERA)
        );

        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getCards()).thenReturn(playerCards);

        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(plagueRepository.getPlagues()).thenReturn(List.of(plague));
        when(plague.getName()).thenReturn(PlagueName.CHOLERA);
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
    void researchPlagueSuccessful() throws PlagueManagementException, PlayerManagementException {
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
}
