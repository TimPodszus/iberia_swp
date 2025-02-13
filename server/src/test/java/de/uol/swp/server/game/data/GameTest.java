package de.uol.swp.server.game.data;

import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.EpidemicCard;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForConfirmationState;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
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
                48 + game.getDifficulty() + 3,
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
                48 + game.getDifficulty() + 3,
                game.getPlayerCardDrawPile()
                    .size()
        );
    }

    @Test
    void testGetCurrentPlayer() {
        IUser user = new User("testUser", "testPassword");
        game.getPlayers()
            .add(new Player(user));
        assertNotNull(game.getCurrentPlayer());
    }

    @Test
    void testSetState() {
        PlayerTurnState playerTurnState = new PlayerTurnState();
        WaitForConfirmationState waitForConfirmationState = new WaitForConfirmationState();
        game.setState(playerTurnState);
        assertEquals(playerTurnState, game.getState());
        game.setState(waitForConfirmationState);
        assertEquals(waitForConfirmationState, game.getState());
        assertEquals(playerTurnState, game.getPreviousState());
    }
}

