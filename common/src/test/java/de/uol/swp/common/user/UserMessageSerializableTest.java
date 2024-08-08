package de.uol.swp.common.user;

import de.uol.swp.common.SerializationTestHelper;
import de.uol.swp.common.user.exception.RegistrationExceptionMessage;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.message.UsersListMessage;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.common.user.request.LogoutRequest;
import de.uol.swp.common.user.request.RegisterUserRequest;
import de.uol.swp.common.user.request.RetrieveAllOnlineUsersRequest;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMessageSerializableTest {

    private static final User defaultUser = new UserDTO("marco", "marco", "marco@grawunder.de");

    private static final int SIZE = 10;
    private static final List<String> users = new ArrayList<>();

    static {
        for (int i = 0; i < SIZE; i++) {
            users.add("User" + i);
        }
    }

    @Test
    void testUserMessagesSerializable() {
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new UserLoggedInMessage("test"),
                UserLoggedInMessage.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new UserLoggedOutMessage("test"),
                UserLoggedOutMessage.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new UsersListMessage(users),
                UsersListMessage.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new RegistrationExceptionMessage("Error"),
                RegistrationExceptionMessage.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new LoginSuccessfulResponse(defaultUser),
                LoginSuccessfulResponse.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new LoginRequest("name", "pass"),
                LoginRequest.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new LogoutRequest(), LogoutRequest.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new RegisterUserRequest(defaultUser),
                RegisterUserRequest.class));
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable(new RetrieveAllOnlineUsersRequest(),
                RetrieveAllOnlineUsersRequest.class));

    }
}
