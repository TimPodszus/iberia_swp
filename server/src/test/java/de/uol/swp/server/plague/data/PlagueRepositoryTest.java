package de.uol.swp.server.plague.data;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link PlagueRepository}.
 * Ensures that the repository correctly initializes and provides access to the available plagues.
 */
class PlagueRepositoryTest {

    /**
     * Instance of {@link PlagueRepository} used in the tests.
     */
    private PlagueRepository plagueRepository;

    /**
     * Sets up the test environment by initializing the {@link PlagueRepository}.
     */
    @BeforeEach
    void setUp() {
        plagueRepository = new PlagueRepository();
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagues()} method returns the correct number of plagues.
     * Verifies that exactly four plagues are present in the repository.
     */
    @Test
    void testGetPlaguesReturnsCorrectSize() {
        List<IPlague> plagues = plagueRepository.getPlagues();
        assertEquals(4, plagues.size());
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagues()} method includes Yellow Fever among the available plagues.
     */
    @Test
    void testGetPlaguesContainsYellowFever() {
        List<IPlague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream()
                          .anyMatch(p -> p.getName() == PlagueName.YELLOW_FEVER));
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagues()} method includes Cholera among the available plagues.
     */
    @Test
    void testGetPlaguesContainsCholera() {
        List<IPlague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream()
                          .anyMatch(p -> p.getName() == PlagueName.CHOLERA));
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagues()} method includes Malaria among the available plagues.
     */
    @Test
    void testGetPlaguesContainsMalaria() {
        List<IPlague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream()
                          .anyMatch(p -> p.getName() == PlagueName.MALARIA));
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagues()} method includes Typhus among the available plagues.
     */
    @Test
    void testGetPlaguesContainsTyphus() {
        List<IPlague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream()
                          .anyMatch(p -> p.getName() == PlagueName.TYPHUS));
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagueByName(PlagueName)} method returns the correct plague.
     */
    @Test
    void testGetPlagueByName() {
        IPlague plague = plagueRepository.getPlagueByName(PlagueName.CHOLERA);
        assertNotNull(plague);
        assertEquals(PlagueName.CHOLERA, plague.getName());
    }

    /**
     * Tests whether the {@link PlagueRepository#getPlagueByName(PlagueName)} method returns null for a non-existent plague.
     */
    @Test
    void testGetPlagueByNameNotFound() {
        IPlague plague = plagueRepository.getPlagueByName(null);
        assertNull(plague);
    }
}