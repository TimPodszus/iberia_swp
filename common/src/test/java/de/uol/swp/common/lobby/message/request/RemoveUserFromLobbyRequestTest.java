package de.uol.swp.common.lobby.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveUserFromLobbyRequestTest {

    private static final String LOBBY_ID = "testID";
    private static final String USER_TO_REMOVE = "testUser";

    @Test
    void testConstructor() {
        RemoveUserFromLobbyRequest request = new RemoveUserFromLobbyRequest(LOBBY_ID, USER_TO_REMOVE);

        assertEquals(LOBBY_ID, request.getLobbyId());
        assertEquals(USER_TO_REMOVE, request.getUserToRemove());
    }
}
