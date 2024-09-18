package de.uol.swp.server.Game;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.GameTurnException;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceWaterTreatmentTest
{
    Player player;
    Board board;
    GameTurn gameTurn;
    Region region;

    @BeforeEach
    void setUp()
    {
        player = new Player(null, null, null, null);
        board = new Board(null, 0, 0, null, null, null, null, 2, 0);
        gameTurn = new GameTurn(player, board);
        region = new Region(0, null);
    }

    @Test
    void testPlaceWaterTreatment_Success() throws GameTurnException
    {
        gameTurn.placeWaterTreatment(region, 2);

        assertEquals(0, board.getWaterTreatmentsLeft());
        assertEquals(2, region.getWaterTreatments());
    }

    @Test
    void testPlaceWaterTreatment_NotEnoughMarkers_ThrowsException()
    {
        GameTurnException exception = assertThrows(GameTurnException.class,
                () -> gameTurn.placeWaterTreatment(region, 3)
        );

        assertEquals("Es sind nicht mehr genug Wasseraufbereitungsmarker vorhanden!", exception.getMessage());
    }
}
