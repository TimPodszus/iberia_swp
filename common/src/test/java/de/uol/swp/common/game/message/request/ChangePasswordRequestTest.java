package de.uol.swp.common.game.message.request;

import de.uol.swp.common.user.IUserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ChangePasswordRequestTest {

    @Test
    void testChangePasswordRequest() {
        IUserDTO mockUser = mock(IUserDTO.class);
        String newPassword = "newPassword123";

        ChangePasswordRequest request = new ChangePasswordRequest(mockUser, newPassword);

        assertEquals(mockUser, request.getUserDTO());
        assertEquals(newPassword, request.getNewPassword());
    }
}