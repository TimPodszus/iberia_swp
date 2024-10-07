package de.uol.swp.server.lobby.management;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

/**
 * Test class for LobbyManagement.
 */
class LobbyManagementTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final UserDTO user1 = new UserDTO("Lasse", "Klasse");
    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore;
    LobbyManagement lobbyManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userList.add(firstOwner);
        userList.add(user1);

        lobbyStore = mock(LobbyStore.class);
        lobbyManagement = new LobbyManagement(lobbyStore);
    }

    /**
     * Tests the creation of a lobby.
     *
     * @throws LobbyManagementException if there is an error in lobby management
     * @throws SQLException             if there is an SQL error
     */
    @Test
    void createLobbyTest() throws LobbyManagementException, SQLException {
        ILobby lobby = new Lobby("testcode", "Test", List.of(firstOwner), firstOwner, 4);

        when(lobbyStore.createLobby(anyString(), eq("Test"), anyList(), eq(firstOwner), eq(4))).thenReturn(lobby);

        ILobby createdLobby = lobbyManagement.createLobby("Test", firstOwner);

        assertNotNull(createdLobby);
        assertEquals(firstOwner.getUsername(),
                createdLobby.getOwner()
                            .getUsername()
        );
    }

    /**
     * Tests the failure of lobby creation.
     *
     * @throws SQLException if there is an SQL error
     */
    @Test
    void failedCreateLobbyTest() throws SQLException {
        when(lobbyStore.createLobby(anyString(),
                eq("Test"),
                anyList(),
                eq(firstOwner),
                eq(4)
        )).thenThrow(new SQLException());

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.createLobby("Test", firstOwner),
                "Should throw an exception when trying to create a lobby."
        );

        assertEquals("Failed to create lobby: null", thrown.getMessage());
    }

    /**
     * Tests the deletion of a lobby.
     *
     * @throws SQLException if there is an SQL error
     */
    @Test
    void deleteLobbyTest() throws SQLException {
        Lobby mockLobby = new Lobby("testcode2", "test2", userList, user1, 3);

        when(lobbyStore.findLobby("testcode2")).thenReturn(mockLobby);

        assertDoesNotThrow(() -> lobbyManagement.deleteLobby("testcode2"));
    }

    /**
     * Tests the failure of lobby deletion.
     *
     * @throws SQLException if there is an SQL error
     */
    @Test
    void deleteLobbyTestFailed() throws SQLException {
        when(lobbyStore.findLobby("testcode2")).thenThrow(new SQLException());

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.deleteLobby("testcode2"),
                "Should throw an exception when trying to delete a lobby."
        );

        assertEquals("Failed to delete lobby", thrown.getMessage());
    }

    /**
     * Tests the deletion of a non-existent lobby.
     *
     * @throws SQLException if there is an SQL error
     */
    @Test
    void deleteNonExistendLobbyTest() throws SQLException {
        when(lobbyStore.findLobby("NonExistentLobby")).thenReturn(null);

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.deleteLobby("NonExistentLobby"),
                "Should throw an exception when trying to delete a non-existent lobby."
        );

        assertEquals("LobbyID NonExistentLobby not found!", thrown.getMessage());
    }

    /**
     * Tests the retrieval of a lobby.
     *
     * @throws SQLException             if there is an SQL error
     * @throws LobbyManagementException if there is an error in lobby management
     */
    @Test
    void getLobbyTest() throws SQLException, LobbyManagementException {
        ILobby mockLobby = new Lobby("testcode2", "Test2", userList, user1, 3);

        when(lobbyStore.findLobby("testcode2")).thenReturn(mockLobby);

        ILobby foundLobby = lobbyManagement.getLobby("testcode2")
                                           .orElse(null);

        assertNotNull(foundLobby);
        assertEquals(user1.getUsername(),
                foundLobby.getOwner()
                          .getUsername()
        );
    }

    /**
     * Tests the retrieval of a non-existent lobby.
     *
     * @throws SQLException             if there is an SQL error
     * @throws LobbyManagementException if there is an error in lobby management
     */
    @Test
    void getNonExistentLobbyTest() throws SQLException, LobbyManagementException {
        when(lobbyStore.findLobby("NonExistentLobby")).thenReturn(null);

        assertTrue(lobbyManagement.getLobby("NonExistentLobby")
                                  .isEmpty());
    }

    /**
     * Tests the failure of lobby retrieval.
     *
     * @throws SQLException if there is an SQL error
     */
    @Test
    void failedGetLobbyTest() throws SQLException {
        when(lobbyStore.findLobby("testcode2")).thenThrow(new SQLException());

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.getLobby("testcode2"),
                "Should throw an exception when trying to get a lobby."
        );

        assertEquals("Failed to get lobby", thrown.getMessage());
    }

    @Test
    void getLobbiesTest() throws SQLException {
        Map<String, ILobby> lobbies = new HashMap<>();
        ILobby lobby1 = new Lobby("testcode1", "Test1", userList, firstOwner, 4);
        ILobby lobby2 = new Lobby("testcode2", "Test2", userList, user1, 3);
        lobbies.put(lobby1.getLobbyCode(), lobby1);
        lobbies.put(lobby2.getLobbyCode(), lobby2);

        when(lobbyStore.getAllLobbies()).thenReturn(lobbies);

        List<ILobby> foundLobbies = assertDoesNotThrow(() -> lobbyManagement.getLobbies());

        assertNotNull(foundLobbies);
        assertEquals(2, foundLobbies.size());
    }

    @Test
    void failedGetLobbiesTest() throws SQLException {
        when(lobbyStore.getAllLobbies()).thenThrow(new SQLException());

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.getLobbies(),
                "Should throw an exception when trying to get lobbies."
        );

        assertEquals("Failed to get lobbies", thrown.getMessage());
    }

    @Test
    void userLeavesLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = new Lobby("Test4", "code4", userList, firstOwner, 3);
        when(lobbyStore.findLobby("code4")).thenReturn(mockLobby);
        lobbyManagement.leaveLobby("code4", user1);
        mockLobby.leaveUser(user1);
        assertFalse(mockLobby.getUsers()
                             .contains(user1));
        verify(lobbyStore).removeUser("code4", user1);
        verify(lobbyStore, never()).removeLobby("code4");
    }

    @Test
    void ownerLeavesLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = new Lobby("Test5", "code5", userList, firstOwner, 3);
        when(lobbyStore.findLobby("code5")).thenReturn(mockLobby);
        lobbyManagement.leaveLobby("code5", firstOwner);
        mockLobby.leaveUser(firstOwner);
        assertFalse(mockLobby.getUsers()
                             .contains(firstOwner));
        verify(lobbyStore).removeUser("code5", firstOwner);
        verify(lobbyStore, never()).removeLobby("code5");
        assertEquals(user1, mockLobby.getOwner());
    }

    @Test
    void allUsersLeaveLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = mock(Lobby.class);
        List<User> users = new ArrayList<>();
        users.add(firstOwner);
        users.add(user1);
        when(mockLobby.getUsers()).thenReturn(users);
        when(mockLobby.getOwner()).thenReturn(firstOwner);
        when(lobbyStore.findLobby("code6")).thenReturn(mockLobby);
        lobbyManagement.leaveLobby("code6", firstOwner);
        mockLobby.leaveUser(firstOwner);
        assertFalse(mockLobby.getUsers()
                             .contains(firstOwner));
        users.remove(firstOwner);
        when(mockLobby.getUsers()).thenReturn(users);
        lobbyManagement.leaveLobby("code6", user1);
        mockLobby.leaveUser(user1);
        assertFalse(mockLobby.getUsers()
                             .contains(user1));
        assertTrue(mockLobby.getUsers()
                            .isEmpty());
        users.remove(user1);
        when(mockLobby.getUsers()).thenReturn(users);
        verify(lobbyStore).removeUser("code6", firstOwner);
        verify(lobbyStore).removeUser("code6", user1);
        verify(lobbyStore).removeLobby("code6");
    }


    /**
     * Tests the update of a lobby.
     *
     * @throws LobbyManagementException if there is an error in lobby management
     */
    @Test
    void updateLobbyTest() throws LobbyManagementException, SQLException {
        ILobby lobby = new Lobby("testcode", "Test", List.of(firstOwner), firstOwner, 4);

        lobbyManagement.updateLobby(lobby);

        verify(lobbyStore, atLeast(1)).updateLobby(eq("Test"), eq("testcode"), anyList(), eq(firstOwner), eq(4));
    }

    /**
     * Tests the failure of lobby update.
     */
    @Test
    void failedUpdateLobbyTest() throws SQLException {
        ILobby lobby = new Lobby("testcode", "Test", List.of(firstOwner), firstOwner, 4);

        when(lobbyStore.updateLobby(eq("Test"),
                eq("testcode"),
                anyList(),
                eq(firstOwner),
                eq(4)
        )).thenThrow(new SQLException());

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.updateLobby(lobby),
                "Should throw an exception when trying to update a lobby."
        );

        assertEquals("Failed to update lobby", thrown.getMessage());
    }
}
