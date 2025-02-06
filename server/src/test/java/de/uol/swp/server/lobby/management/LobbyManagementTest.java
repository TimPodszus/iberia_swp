package de.uol.swp.server.lobby.management;


import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.store.ILobbyStore;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for LobbyManagement.
 */
class LobbyManagementTest {

    static final IUser firstOwner = new User("Marco", "Marco");
    static final IUser user1 = new User("Lasse", "Klasse");
    List<IUser> userList = new ArrayList<>();
    ILobbyStore lobbyStore;
    LobbyManagement lobbyManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lobbyManagement = new LobbyManagement();
        lobbyStore = LobbyStore.getInstance();
        userList.add(firstOwner);
        userList.add(user1);
    }

    /**
     * Tests the creation of a lobby.
     */
    @Test
    void createLobbyTest() {
        ILobby createdLobby = lobbyManagement.createLobby(firstOwner);

        assertNotNull(createdLobby);
        assertEquals(
                firstOwner.getUsername(),
                createdLobby.getOwner()
                            .getUsername()
        );
    }

    /**
     * Tests the deletion of a lobby.
     */
    @Test
    void deleteLobbyTest() {
        Lobby lobby = new Lobby("testcode2", "test2", userList, user1, 3);
        lobbyStore.saveLobby(lobby);
        int size = lobbyStore.getAllLobbies()
                             .size();
        lobbyManagement.deleteLobby("testcode2");
        assertEquals(
                size - 1,
                lobbyStore.getAllLobbies()
                          .size()
        );
    }

    /**
     * Tests the deletion of a non-existent lobby.
     */
    @Test
    void deleteNonExistendLobbyTest() {
        Lobby lobby = new Lobby("testcode2", "test2", userList, user1, 3);
        lobbyStore.saveLobby(lobby);
        int size = lobbyStore.getAllLobbies()
                             .size();
        lobbyManagement.deleteLobby("NonExistentLobby");

        assertEquals(
                size,
                lobbyStore.getAllLobbies()
                          .size()
        );
    }

    /**
     * Tests the retrieval of a lobby.
     */
    @Test
    void getLobbyTest() {
        ILobby lobby = new Lobby("testcode2", "Test2", userList, user1, 3);

        lobbyStore.saveLobby(lobby);

        ILobby foundLobby = lobbyManagement.getLobby("testcode2");

        assertNotNull(foundLobby);
        assertEquals(
                user1.getUsername(),
                foundLobby.getOwner()
                          .getUsername()
        );
    }

    /**
     * Tests the retrieval of a non-existent lobby.
     */
    @Test
    void getNonExistentLobbyTest() {
        assertNull(lobbyManagement.getLobby("NonExistentLobby"));
    }

    /**
     * Tests the retrieval of all lobbies.
     */
    @Test
    void getLobbiesTest() {
        ILobby lobby1 = new Lobby("testcode1", "Test1", userList, firstOwner, 4);
        ILobby lobby2 = new Lobby("testcode2", "Test2", userList, user1, 3);
        lobbyStore.saveLobby(lobby1);
        lobbyStore.saveLobby(lobby2);

        List<ILobby> foundLobbies = assertDoesNotThrow(() -> lobbyManagement.getLobbies());

        assertNotNull(foundLobbies);
        assertEquals(2, foundLobbies.size());
    }

    /**
     * Tests a successful user joining a lobby.
     */
    @Test
    void joinLobby_Success() throws LobbyStoreException {
        User user = new User("testUser", "testUser");
        Lobby lobby = new Lobby("testLobbyCode", "testLobbyName", new ArrayList<>(List.of(firstOwner)), firstOwner, 4);
        lobbyStore.saveLobby(lobby);

        lobbyManagement.joinLobby("testLobbyCode", user);

        assertEquals(
                2,
                lobby.getUsers()
                     .size()
        );
    }

    /**
     * Tests a user leaving a lobby.
     */
    @Test
    void userLeavesLobbyTest() {
        Lobby mockLobby = new Lobby("Test4", "code4", new ArrayList<>(List.of(firstOwner, user1)), firstOwner, 3);
        lobbyStore.saveLobby(mockLobby);

        lobbyManagement.leaveLobby("Test4", user1);

        assertFalse(mockLobby.getUsers()
                             .contains(user1));
    }

    /**
     * Tests the owner leaving a lobby.
     */
    @Test
    void ownerLeavesLobbyTest() {
        Lobby mockLobby = new Lobby("Test5", "code5", userList, firstOwner, 3);
        lobbyStore.saveLobby(mockLobby);
        lobbyManagement.leaveLobby("Test5", firstOwner);
        mockLobby.removeUser(firstOwner);
        assertFalse(mockLobby.getUsers()
                             .contains(firstOwner));
        assertEquals(user1, mockLobby.getOwner());
    }

    /**
     * Tests all users leaving a lobby.
     */
    @Test
    void allUsersLeaveLobbyTest() {
        ILobby mockLobby = new Lobby("Test6", "code6", new ArrayList<>(List.of(firstOwner, user1)), firstOwner, 3);
        lobbyStore.saveLobby(mockLobby);

        lobbyManagement.leaveLobby("Test6", firstOwner);
        lobbyManagement.leaveLobby("Test6", user1);

        assertFalse(mockLobby.getUsers()
                             .contains(user1));
        assertTrue(mockLobby.getUsers()
                            .isEmpty());
    }

    /**
     * Tests the update of a lobby.
     */
    @Test
    void updateLobbyTest() {
        ILobby lobby = new Lobby("testcode", "Test", List.of(firstOwner), firstOwner, 4);
        lobbyStore.saveLobby(lobby);
        ILobby updatedLobby = new Lobby("testcode", "Updated", List.of(firstOwner, user1), firstOwner, 4);
        lobbyManagement.updateLobby(updatedLobby);

        assertEquals(updatedLobby, lobbyManagement.getLobby("testcode"));
    }

    /**
     * Tests joining a non-existent lobby.
     */
    @Test
    void joinLobby_LobbyNotFound() {
        User user = new User("testUser", "testUser");

        assertThrows(LobbyStoreException.class, () -> lobbyManagement.joinLobby("testLobbyCode", user));
    }

    /**
     * Tests removing a user from a lobby.
     */
    @Test
    void testRemoveUser() throws LobbyStoreException {
        ILobby lobby = new Lobby("testcode", "Test", userList, firstOwner, 4);
        lobbyStore.saveLobby(lobby);
        ILobby updatedLobby = lobbyManagement.removeUser("testcode", "Lasse");

        assertEquals(
                1,
                updatedLobby.getUsers()
                            .size()
        );
    }

    /**
     * Tests removing a user from an invalid lobby.
     */
    @Test
    void testRemoveUserWithInvalidLobby() {
        assertThrows(LobbyStoreException.class, () -> lobbyManagement.removeUser("test", "Lasse"));
    }

    /**
     * Tests removing a user with an invalid username.
     */
    @Test
    void testRemoveUserWithWrongUsername() throws LobbyStoreException {
        ILobby lobby = new Lobby("testcode", "Test", userList, firstOwner, 4);
        lobbyStore.saveLobby(lobby);
        ILobby updatedLobby = lobbyManagement.removeUser("testcode", "invalid");

        assertEquals(
                2,
                updatedLobby.getUsers()
                            .size()
        );
    }
}