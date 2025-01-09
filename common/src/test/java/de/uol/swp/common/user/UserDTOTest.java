package de.uol.swp.common.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test Class for the UserDTO
 *
 * @author Marco Grawunder
 * @since 2019-09-04
 */
class UserDTOTest {

    private static final IUserDTO defaultUser = new UserDTO("marco", "marco");
    private static final IUserDTO secondUser = new UserDTO("marco2", "marco");




    /**
     * This test checks if the createWithoutPassword function generates the Object correctly
     * This test fails if the usernames or emails do not match or the password is not empty.
     * @since 2019-09-04
     */
    @Test
    void createWithExistingUserWithoutPassword() {
        IUserDTO newUser = UserDTO.createWithoutPassword(defaultUser);

        // Test every attribute
        assertEquals(defaultUser.getUsername(), newUser.getUsername());
        assertEquals("", newUser.getPassword());

    }

    /**
     * This test checks if the getWithoutPassword function generates the Object correctly
     * This test fails if the usernames do not match or the password is not empty.
     * @since 2019-09-04
     */
    @Test
    void getWithoutPassword() {
        IUserDTO userWithoutPassword = defaultUser.getWithoutPassword();

        assertEquals("", userWithoutPassword.getPassword());
        assertEquals(defaultUser.getUsername(), userWithoutPassword.getUsername());
    }

    /**
     * Test if two different users are equal
     * This test fails if they are considered equal
     * @since 2019-09-04
     */
    @Test
    void usersNotEquals_User() {
        assertNotEquals(defaultUser, secondUser);
    }

     /**
     * Test of compare function
     * This test compares two different users. It fails if the function returns
     * that both of them are equal.
     * @since 2019-09-04
     */
    @Test
    void userCompare() {
        assertEquals(defaultUser.compareTo((UserDTO) secondUser), -1);
    }


}