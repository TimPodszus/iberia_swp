package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlagueRepositoryTest {
    private PlagueRepository plagueRepository;

    @BeforeEach
    void setUp() {
        plagueRepository = new PlagueRepository();
    }

    @Test
    void testGetPlaguesReturnsCorrectSize() {
        List<Plague> plagues = plagueRepository.getPlagues();
        assertEquals(4, plagues.size());
    }

    @Test
    void testGetPlaguesContainsYellowFever() {
        List<Plague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream().anyMatch(p -> p.getName() == PlagueName.YELLOW_FEVER));
    }

    @Test
    void testGetPlaguesContainsCholera() {
        List<Plague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream().anyMatch(p -> p.getName() == PlagueName.CHOLERA));
    }

    @Test
    void testGetPlaguesContainsMalaria() {
        List<Plague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream().anyMatch(p -> p.getName() == PlagueName.MALARIA));
    }

    @Test
    void testGetPlaguesContainsTyphus() {
        List<Plague> plagues = plagueRepository.getPlagues();
        assertTrue(plagues.stream().anyMatch(p -> p.getName() == PlagueName.TYPHUS));
    }
}
