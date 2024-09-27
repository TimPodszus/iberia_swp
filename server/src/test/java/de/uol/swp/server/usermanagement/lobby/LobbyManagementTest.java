package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.Lobby;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.lobby.LobbyManagementException;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LobbyManagementTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final UserDTO user1 = new UserDTO("Lasse", "Klasse");
    static final UserDTO user2 = new UserDTO("UserZwei", "zwei");
    final LobbyManagement lobbyManagement = new LobbyManagement();
    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore = new LobbyStore();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userList.add(firstOwner);
        userList.add(user1);

        lobbyStore = mock(LobbyStore.class);
        lobbyManagement.setLobbyStore(lobbyStore);
    }


    @Test
    void getLobbyTest() throws SQLException {
        Lobby mockLobby = new Lobby("Test2", "testcode2", userList, user1, 3);

        when(lobbyStore.findLobby("Test2")).thenReturn(mockLobby);

        Lobby foundLobby = lobbyManagement.getLobby("Test2").orElse(null);

        assertNotNull(foundLobby);
        assertEquals(user1.getUsername(), foundLobby.getOwner().getUsername());
    }


    @Test
    void createLobbyWithExistingNameThrowsExceptionTest() throws SQLException {
        Lobby mockLobby = new Lobby("Test1", "testcode", userList, user2, 3);

        when(lobbyStore.findLobby("Test1")).thenReturn(mockLobby);

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.createLobby("Test1", user2),
                "Should throw an exception when lobby name already exists.");

        assertEquals("Lobby name Test1 already exists!", thrown.getMessage());
    }

    @Test
    void dropNonExistentLobbyThrowsExceptionTest() throws SQLException {
        when(lobbyStore.findLobby("NonExistentLobby")).thenReturn(null);

        LobbyManagementException thrown = assertThrows(LobbyManagementException.class,
                () -> lobbyManagement.dropLobby("NonExistentLobby"),
                "Should throw an exception when trying to delete a non-existent lobby.");

        assertEquals("LobbyID NonExistentLobby not found!", thrown.getMessage());
    }

    @Test
    void dropLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = new Lobby("Test3", "testcode2", userList, firstOwner, 3);

        when(lobbyStore.findLobby("Test3")).thenReturn(mockLobby);

        lobbyStore.removeLobby("Test3");

        when(lobbyStore.findLobby("Test3")).thenReturn(null);

        assertTrue(lobbyManagement.getLobby("Test3").isEmpty());
    }

    @Test
    void userLeavesLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = new Lobby("Test4", "code4", userList, firstOwner, 3);

        when(lobbyStore.findLobby("code4")).thenReturn(mockLobby);

        lobbyManagement.leaveLobby("code4", user1);

        mockLobby.leaveUser(user1);

        assertFalse(mockLobby.getUsers().contains(user1));

        verify(lobbyStore).removeUser("code4", user1);

        verify(lobbyStore, never()).removeLobby("code4");
    }

    @Test
    void ownerLeavesLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = new Lobby("Test5", "code5", userList, firstOwner, 3);

        when(lobbyStore.findLobby("code5")).thenReturn(mockLobby);

        lobbyManagement.leaveLobby("code5", firstOwner);

        mockLobby.leaveUser(firstOwner);

        assertFalse(mockLobby.getUsers().contains(firstOwner));

        verify(lobbyStore).removeUser("code5", firstOwner);

        verify(lobbyStore, never()).removeLobby("code5");

        assertEquals(user1, mockLobby.getOwner());
    }

    @Test
    void allUsersLeaveLobbyTest() throws SQLException, LobbyStoreException {
        Lobby mockLobby = mock(Lobby.class);
        HashSet<User> users = new HashSet<>();
        users.add(firstOwner);
        users.add(user1);

        when(mockLobby.getUsers()).thenReturn(users);
        when(mockLobby.getOwner()).thenReturn(firstOwner);
        when(lobbyStore.findLobby("code6")).thenReturn(mockLobby);


        lobbyManagement.leaveLobby("code6", firstOwner);
        mockLobby.leaveUser(firstOwner);
        assertFalse(mockLobby.getUsers().contains(firstOwner));

        users.remove(firstOwner);

        when(mockLobby.getUsers()).thenReturn(users);
        lobbyManagement.leaveLobby("code6", user1);

        mockLobby.leaveUser(user1);
        assertFalse(mockLobby.getUsers().contains(user1));
        assertTrue(mockLobby.getUsers().isEmpty());

        users.remove(user1);
        when(mockLobby.getUsers()).thenReturn(users);

        verify(lobbyStore).removeUser("code6", firstOwner);
        verify(lobbyStore).removeUser("code6", user1);
        verify(lobbyStore).removeLobby("code6");
    }



}
