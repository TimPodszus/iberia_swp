package de.uol.swp.server.lobby.management;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.lobby.store.LobbyStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LobbyManagementTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final UserDTO user1 = new UserDTO("Lasse", "Klasse");
    static final UserDTO user2 = new UserDTO("UserZwei", "zwei");
    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore;
    LobbyManagement lobbyManagement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userList.add(firstOwner);
        userList.add(user1);

        lobbyStore = mock(LobbyStore.class);
        lobbyManagement = new LobbyManagement(lobbyStore);
    }


    @Test
    void getLobbyTest() throws SQLException, LobbyManagementException {
        ILobby mockLobby = new Lobby("Test2", "testcode2", userList, user1, 3);

        when(lobbyStore.findLobby("Test2")).thenReturn(mockLobby);

        ILobby foundLobby = lobbyManagement.getLobby("Test2")
                                           .orElse(null);

        assertNotNull(foundLobby);
        assertEquals(
                user1.getUsername(),
                foundLobby.getOwner()
                          .getUsername()
        );
    }


    @Test
    void createLobbyWithExistingNameThrowsExceptionTest() throws SQLException {
        Lobby mockLobby = new Lobby("Test1", "testcode", userList, user2, 3);

        when(lobbyStore.findLobby("Test1")).thenReturn(mockLobby);

        LobbyManagementException thrown = assertThrows(
                LobbyManagementException.class,
                () -> lobbyManagement.createLobby("Test1", user2),
                "Should throw an exception when lobby name already exists."
        );

        assertEquals("Lobby name Test1 already exists!", thrown.getMessage());
    }

    @Test
    void dropNonExistentLobbyThrowsExceptionTest() throws SQLException {
        when(lobbyStore.findLobby("NonExistentLobby")).thenReturn(null);

        LobbyManagementException thrown = assertThrows(
                LobbyManagementException.class,
                () -> lobbyManagement.deleteLobby("NonExistentLobby"),
                "Should throw an exception when trying to delete a non-existent lobby."
        );

        assertEquals("LobbyID NonExistentLobby not found!", thrown.getMessage());
    }

    @Test
    void dropLobbyTest() throws SQLException, LobbyManagementException {
        Lobby mockLobby = new Lobby("Test3", "testcode2", userList, firstOwner, 3);

        when(lobbyStore.findLobby("Test3")).thenReturn(mockLobby);

        lobbyStore.removeLobby("Test3");

        when(lobbyStore.findLobby("Test3")).thenReturn(null);

        assertTrue(lobbyManagement.getLobby("Test3")
                                  .isEmpty());
    }

}
