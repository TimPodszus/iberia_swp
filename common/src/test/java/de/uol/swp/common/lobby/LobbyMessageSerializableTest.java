package de.uol.swp.common.lobby;

import de.uol.swp.common.SerializationTestHelper;
import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.message.request.LobbyLeaveUserRequest;
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
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new LobbyJoinUserRequest("test"),
                LobbyJoinUserRequest.class
        ));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new LobbyLeaveUserRequest("test"),
                LobbyLeaveUserRequest.class
        ));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new UserJoinedLobbyMessage("test",
                defaultUser
        ), UserJoinedLobbyMessage.class));
    }


}
