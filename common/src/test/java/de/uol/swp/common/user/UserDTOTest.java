package de.uol.swp.common.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test Class for the UserDTO
 *
 * @author Marco Grawunder
 * @since 2019-09-04
 */
class UserDTOTest {

    private static final User defaultUser = new UserDTO("marco", "marco");
    private static final User secondsUser = new UserDTO("marco2", "marco");

    /**
     * This test check whether the username can be null
     * If the constructor does not throw an Exception the test fails
     * @since 2019-09-04
     */
    @Test
    void createUserWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new UserDTO(null, ""));
    }

    /**
     * This test check whether the password can be null
     * If the constructor does not throw an Exception the test fails
     * @since 2019-09-04
     */
    @Test
    void createUserWithEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> new UserDTO("", null));
    }



    /**
     * This test checks if the createWithoutPassword function generates the Object correctly
     * This test fails if the usernames or emails do not match or the password is not empty.
     * @since 2019-09-04
     * The Hash {SHA512}z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==
     * equals an Empty String
     */
    @Test
    void createWithExistingUserWithoutPassword() {
        User newUser = UserDTO.createWithoutPassword(defaultUser);

        // Test every attribute
        assertEquals(defaultUser.getUsername(), newUser.getUsername());
        assertEquals("{SHA512}z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==", newUser.getPassword());


        // Test with equals method
        assertEquals(defaultUser, newUser);
    }

    /**
     * This test checks if the getWithoutPassword function generates the Object correctly
     * This test fails if the usernames do not match or the password is not empty.
     * @since 2019-09-04
     * The Hash {SHA512}z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==
     * equals an Empty String
     */
    @Test
    void getWithoutPassword() {
        User userWithoutPassword = defaultUser.getWithoutPassword();

        assertEquals("{SHA512}z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==", userWithoutPassword.getPassword());
        assertEquals(defaultUser.getUsername(), userWithoutPassword.getUsername());
    }

    /**
     * Test if two different users are equal
     * This test fails if they are considered equal
     * @since 2019-09-04
     */
    @Test
    void usersNotEquals_User() {
        assertNotEquals(defaultUser, secondsUser);
    }

     /**
     * Test of compare function
     * This test compares two different users. It fails if the function returns
     * that both of them are equal.
     * @since 2019-09-04
     */
    @Test
    void userCompare() {
        assertEquals(defaultUser.compareTo(secondsUser), -1);
    }


}