package de.uol.swp.server.lobby.data;

import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Lobby functionality.
 */
public class LobbyTest {
    IUser owner = new User("owner", "testpassword");
    IUser user = new User("user", "testpassword");

    /**
     * Tests the creation of a lobby.
     */
    @Test
    void createLobbyTest() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        assertEquals("testcode", lobby.getLobbyId());
        assertEquals("Test", lobby.getName());
        assertEquals(4, lobby.getDifficulty());
        assertEquals(owner, lobby.getOwner());
        assertEquals(
                2,
                lobby.getUsers()
                     .size()
        );
    }

    /**
     * Tests updating the owner of the lobby.
     */
    @Test
    void testUpdateOwner() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        lobby.updateOwner(user);
        assertEquals(user, lobby.getOwner());
    }

    /**
     * Tests adding a user to the lobby.
     */
    @Test
    void testAddUser() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner)), owner, 4);
        lobby.addUser(user);
        assertEquals(
                2,
                lobby.getUsers()
                     .size()
        );
    }

    /**
     * Tests removing a user from the lobby.
     */
    @Test
    void testRemoveUser() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        lobby.removeUser(user);
        assertEquals(
                1,
                lobby.getUsers()
                     .size()
        );
    }

    /**
     * Tests retrieving a user from the lobby by username.
     */
    @Test
    void testGetUser() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        assertEquals(user, lobby.getUser("user"));
    }

    /**
     * Tests the equality of two lobbies with the same properties.
     */
    @Test
    void testEquals() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        ILobby lobby2 = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        assertEquals(lobby, lobby2);
    }

    /**
     * Tests the equality of a lobby with itself.
     */
    @Test
    void testEqualsWithSameLobby() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        assertEquals(lobby, lobby);
    }

    /**
     * Tests the inequality of a lobby with a different object.
     */
    @Test
    void testEqualsWithDifferentObject() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        Object obj = new Object();
        assertNotEquals(lobby, obj);
    }

    /**
     * Tests the hash code of the lobby.
     */
    @Test
    void testHashCode() {
        ILobby lobby = new Lobby("testcode", "Test", new ArrayList<>(List.of(owner, user)), owner, 4);
        assertEquals("testcode".hashCode(), lobby.hashCode());
    }
}
