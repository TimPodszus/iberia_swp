package de.uol.swp.server.game.data;

import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.EpidemicCard;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private IGame game;

    @BeforeEach
    void setUp() {
        game = new Game(3, "123");
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getInfectionCardDrawPile());
        assertEquals(1, game.getInfectionCounter());
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
        assertEquals(
                48 + game.getDifficulty(),
                game.getPlayerCardDrawPile()
                    .size()
        );
    }
}

