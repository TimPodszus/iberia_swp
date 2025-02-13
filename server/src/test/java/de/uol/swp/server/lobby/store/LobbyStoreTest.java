package de.uol.swp.server.lobby.store;

import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for the LobbyStore.
 */
class LobbyStoreTest {

    ILobbyStore lobbyStore;

    IUser owner;

    List<IUser> users;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        lobbyStore = LobbyStore.getInstance();
        lobbyStore.removeAll();
        owner = new User("owner", "testpassword");
        users = new ArrayList<>(List.of(owner));
    }

    /**
     * Tests the creation of a lobby.
     */
    @Test
    void testCreateLobby() {
        ILobby lobby = lobbyStore.createLobby("testcode", "Test", users, owner, 3);
        assertEquals("testcode", lobby.getLobbyId());
    }

    /**
     * Tests finding a lobby by its code.
     */
    @Test
    void testFindLobby() {
        lobbyStore.createLobby("findLobby", "FindLobby", users, owner, 3);
        ILobby lobby = lobbyStore.findLobby("findLobby");
        assertEquals("findLobby", lobby.getLobbyId());
        assertEquals("FindLobby", lobby.getName());
    }

    /**
     * Tests finding a lobby with a wrong code.
     */
    @Test
    void testFindLobbyWrongCode() {
        ILobby lobby = lobbyStore.findLobby("wrongCode");
        assertNull(lobby);
    }

    /**
     * Tests removing a lobby.
     */
    @Test
    void testRemoveLobby() {
        lobbyStore.createLobby("removeLobby", "RemoveLobby", users, owner, 3);
        lobbyStore.removeLobby("removeLobby");
        assertNull(lobbyStore.findLobby("removeLobby"));
    }

    /**
     * Tests removing a lobby with a wrong code.
     */
    @Test
    void testRemoveLobbyWrongCode() {
        lobbyStore.createLobby("removeLobby", "RemoveLobby", users, owner, 3);
        lobbyStore.removeLobby("wrongCode");
        assertEquals(
                "removeLobby",
                lobbyStore.findLobby("removeLobby")
                          .getLobbyId()
        );
    }

    @Test
    void testGetAllLobbies() {
        lobbyStore.createLobby("lobby1", "Lobby1", users, owner, 3);
        lobbyStore.createLobby("lobby2", "Lobby2", users, owner, 3);
        assertEquals(
                2,
                lobbyStore.getAllLobbies()
                          .size()
        );
    }

    /**
     * Tests saving a lobby.
     */
    @Test
    void testSaveLobby() {
        lobbyStore.createLobby("saveLobby", "SaveLobby", users, owner, 3);
        ILobby lobby = new Lobby("saveLobby", "ChangedName", users, owner, 3);
        lobbyStore.saveLobby(lobby);
        assertEquals(
                "ChangedName",
                lobbyStore.findLobby("saveLobby")
                          .getName()
        );
    }
}
