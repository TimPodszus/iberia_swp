package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.GameActions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the AvailableActionsResponse class.
 */
public class AvailableActionsResponseTest {
    private static final String LOBBY_ID = "LobbyID";
    private static final String DESCRIPTION = "Description";
    private static final List<GameActions> GAME_ACTIONS = List.of(GameActions.SHARE_KNOWLEDGE,
            GameActions.TREAT_INFECTION
    );

    /**
     * Tests the constructor and getter methods of AvailableActionsResponse.
     * Verifies that the fields are correctly set and retrieved.
     */
    @Test
    void testConstructor() {
        AvailableActionsResponse availableActionsResponse = new AvailableActionsResponse(LOBBY_ID,
                true,
                DESCRIPTION,
                GAME_ACTIONS
        );
        assertEquals(LOBBY_ID, availableActionsResponse.getLobbyId());
        assertEquals(DESCRIPTION, availableActionsResponse.getDescription());
        assertTrue(availableActionsResponse.isSuccess());
        assertEquals(GAME_ACTIONS, availableActionsResponse.getAvailableActions());
    }

    /**
     * Tests the equals method of AvailableActionsResponse.
     * Verifies that two instances with the same field values are considered equal.
     */
    @Test
    void testEquals() {
        AvailableActionsResponse availableActionsResponse = new AvailableActionsResponse(LOBBY_ID,
                true,
                DESCRIPTION,
                GAME_ACTIONS
        );
        AvailableActionsResponse availableActionsResponse1 = new AvailableActionsResponse(LOBBY_ID,
                true,
                DESCRIPTION,
                GAME_ACTIONS
        );
        assertEquals(availableActionsResponse, availableActionsResponse1);
    }

    /**
     * Tests the equals method with the same object.
     * Verifies that an instance is equal to itself.
     */
    @Test
    void testEqualsWithSameObject() {
        AvailableActionsResponse availableActionsResponse = new AvailableActionsResponse(LOBBY_ID,
                true,
                DESCRIPTION,
                GAME_ACTIONS
        );
        assertEquals(availableActionsResponse, availableActionsResponse);
    }

    /**
     * Tests the equals method with a different type of object.
     * Verifies that an instance is not equal to an object of a different type.
     */
    @Test
    void testEqualsWithDifferentObject() {
        AvailableActionsResponse availableActionsResponse = new AvailableActionsResponse(LOBBY_ID,
                true,
                DESCRIPTION,
                GAME_ACTIONS
        );
        Object object = new Object();
        assertNotEquals(availableActionsResponse, object);
    }

    /**
     * Tests the hashCode method of AvailableActionsResponse.
     * Verifies that the hash code of an instance is consistent.
     */
    @Test
    void testHashCode() {
        AvailableActionsResponse availableActionsResponse = new AvailableActionsResponse(LOBBY_ID,
                true,
                DESCRIPTION,
                GAME_ACTIONS
        );
        assertEquals(availableActionsResponse.hashCode(), availableActionsResponse.hashCode());
    }
}
