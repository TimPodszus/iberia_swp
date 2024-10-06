package de.uol.swp.server.game.data;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.EpidemicCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class GameTest {

    private IGame game;
    private CityRepository cityRepository;
    private List<City> mockCities;

    @BeforeEach
    void setUp() {
        cityRepository = new CityRepository();
        mockCities = List.of(
                new City(1, PlagueName.CHOLERA, CityName.BARCELONA, 1234, true),
                new City(1, PlagueName.CHOLERA, CityName.ALBACETE, 111, false)
        );
        game = new Game(3);
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getInfectionCardDrawPile());
        assertEquals(2, game.getInfectionCounter());
        assertEquals(0, game.getEscalationStage());
        assertEquals(14, game.getWaterTreatmentsLeft());
        assertEquals(20, game.getTracksLeft());
        assertFalse(game.getPlayerCardDrawPile()
                        .isEmpty());
        assertNotNull(game.getState());
    }

    @Test
    void testCreateInfectionCards() {
        assertEquals(
                48,
                game.getInfectionCardDrawPile()
                    .size()
        );
    }

    @Test
    void testCreatePlayerCards() {
        assertEquals(
                48 + game.getDifficulty(),
                game.getPlayerCardDrawPile()
                    .size()
        );
        assertTrue(game.getPlayerCardDrawPile()
                       .get(0) instanceof CityCard || game.getPlayerCardDrawPile()
                                                          .get(0) instanceof EpidemicCard);
    }

    @Test
    void testGameStartShuffle() {
        assertEquals(48 + game.getDifficulty(), game.getPlayerCardDrawPile().size());
    }
}

