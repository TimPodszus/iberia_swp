package de.uol.swp.client.lobby.detail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for UserListItem.
 */
class UserListItemTest {

    /**
     * Tests the UserListItem constructor and getName method.
     */
    @Test
    void testUserListItem() {
        UserListItem userListItem = new UserListItem("name");
        assertEquals("name", userListItem.getName());
    }
}
