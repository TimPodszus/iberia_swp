package de.uol.swp.server.Game;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.connection.Connection;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.GameTurnException;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildTrainTrackTest
{
    private Board board;
    private GameTurn gameTurn;
    private Connection connection;

    @BeforeEach
    void setUp()
    {
        Player player = new Player(null, null, null, null);
        board = new Board(null, 0, 0, null, null, null, null, 1, 1);
        connection = new Connection(null, null, false, false, true, false);
        gameTurn = new GameTurn(player, board);
    }

    @Test
    void testBuildTrainTracks_Success() throws GameTurnException
    {
        gameTurn.buildTrainTracks(connection);

        assertTrue(connection.isTrainTrack());
        assertEquals(0, board.getTracksLeft());
    }

    @Test
    void testBuildTrainTracks_NotBuildable_ThrowsException()
    {
        connection = new Connection(null, null, false, false, false, false);

        GameTurnException exception = assertThrows(
                GameTurnException.class,
                () -> gameTurn.buildTrainTracks(connection)
        );
        assertEquals("Auf dieser Verbindung kann keine Zugstrecke gebaut werden", exception.getMessage());
    }

    @Test
    void testBuildTrainTracks_AlreadyHasTrack_ThrowsException()
    {
        connection.setTrainTrack(true);

        GameTurnException exception = assertThrows(
                GameTurnException.class,
                () -> gameTurn.buildTrainTracks(connection)
        );
        assertEquals("Auf dieser Verbindung existiert bereits eine Zugstrecke", exception.getMessage());
    }

    @Test
    void testBuildTrainTracks_NotEnoughTracksLeft_ThrowsException()
    {
        board.setTracksLeft(0);

        GameTurnException exception = assertThrows(
                GameTurnException.class,
                () -> gameTurn.buildTrainTracks(connection)
        );
        assertEquals("Es sind nichtmehr genug Schienen vorhanden!", exception.getMessage());
    }
}
