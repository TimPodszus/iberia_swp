package de.uol.swp.common.lobby;

import de.uol.swp.common.SerializationTestHelper;
import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.request.JoinLobbyRequest;
import de.uol.swp.common.lobby.message.request.LeaveLobbyRequest;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LobbyMessageSerializableTest {

    private static final UserDTO defaultUser = new UserDTO("marco", "marco");

    @Test
    void testLobbyMessagesSerializable() {
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new CreateLobbyRequest(),
                CreateLobbyRequest.class
        ));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new JoinLobbyRequest("test"),
                JoinLobbyRequest.class
        ));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new LeaveLobbyRequest("test"),
                LeaveLobbyRequest.class
        ));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new UserJoinedLobbyMessage("test",
                defaultUser
        ), UserJoinedLobbyMessage.class));
    }


}
