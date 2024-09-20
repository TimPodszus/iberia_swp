package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.Lobby;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.DatabaseBasedUserStore;

import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LobbyManagementTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final UserDTO user1 = new UserDTO("Lasse", "Klasse");
    static final UserDTO user2 = new UserDTO("UserZwei", "zwei");
    final EventBus bus = EventBus.getDefault();
    final UserManagement userManagement = new UserManagement(new DatabaseBasedUserStore());
    final AuthenticationService authService = new AuthenticationService(bus, userManagement);

    final LobbyManagement lobbyManagement = new LobbyManagement();
    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore = new LobbyStore();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lobbyStore = mock(LobbyStore.class);
        userList.add(firstOwner);
        userList.add(user1);
        when(lobbyStore.createLobby("Test", "testcode", userList, firstOwner, 4)).thenReturn(new Lobby("Test", "testcode", userList, firstOwner,4));
        when(lobbyStore.findLobby("testcode")).thenReturn(Optional.of(new Lobby("Test", "testcode", userList, firstOwner,4)));
    }



    @Test
    void getLobbyTest() {
        lobbyManagement.createLobby("Test2", user1);
        if(lobbyManagement.getLobby("Test2").isPresent()){
            assertEquals(user1.getUsername(), lobbyManagement.getLobby("Test2").get().getOwner().getUsername());
        }
    }


    @Test
    void createLobbyWithExistingNameThrowsExceptionTest() { //funktioniert
        // Arrange
        lobbyManagement.createLobby("Test1", user2);

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> lobbyManagement.createLobby("Test1", user2), "Should throw an exception when lobby name already exists.");
        assertEquals("Lobby name Test1 already exists!", thrown.getMessage());
    }

    @Test
    void dropNonExistentLobbyThrowsExceptionTest() { //funktioniert
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> lobbyManagement.dropLobby("NonExistentLobby"), "Should throw an exception when trying to delete a non-existent lobby.");
        assertEquals("Lobby name NonExistentLobby not found!", thrown.getMessage());
    }

    @Test
    void dropLobbyTest() {
        lobbyManagement.createLobby("Test2", firstOwner);
        lobbyManagement.dropLobby("Test2");
        assertTrue(lobbyManagement.getLobby("Test2").isEmpty());
    }

}
