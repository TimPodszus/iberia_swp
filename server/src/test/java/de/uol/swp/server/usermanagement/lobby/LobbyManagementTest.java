package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.Lobby;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.lobby.LobbyService;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnstableApiUsage")
public class LobbyManagementTest
{

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    final EventBus bus = EventBus.getDefault();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final AuthenticationService authService = new AuthenticationService(bus, userManagement);

    final LobbyManagement lobbyManagement = new LobbyManagement();
    final LobbyService lobbyService = new LobbyService(lobbyManagement, authService, bus);
    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore = new LobbyStore();

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        lobbyStore = mock(LobbyStore.class);

        userList.add(firstOwner);
        when(lobbyStore.createLobby("Test", "testcode", userList, 4)).thenReturn(new Lobby("Test", "testcode", userList, 4));

        when(lobbyStore.findLobby("testcode")).thenReturn(Optional.of(new Lobby("Test", "testcode", userList, 4)));
    }

    @Test
    void createLobbyTest() {
        lobbyManagement.createLobby("Test", firstOwner);

        assertNotNull(lobbyManagement.getLobby("Test"));
        if(lobbyManagement.getLobby("Test").isPresent()){
            assertEquals(lobbyManagement.getLobby("Test").get().getOwner(), firstOwner);
        }
    }

    @Test
    void dropLobbyTest() {
        lobbyManagement.createLobby("Test", firstOwner);

        lobbyManagement.dropLobby("Test");

        assertTrue(lobbyManagement.getLobby("Test").isEmpty());
    }

    @Test
    void getLobbyTest() {
        lobbyManagement.createLobby("Test", firstOwner);

        if(lobbyManagement.getLobby("Test").isPresent()){
            assertNotNull(lobbyManagement.getLobby("Test"));
            assertEquals(lobbyManagement.getLobby("Test").get().getOwner(), firstOwner);
        }
    }

    @Test
    void createLobbyWithExistingNameThrowsExceptionTest() {
        // Arrange
        lobbyManagement.createLobby("TestLobby", firstOwner);

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            lobbyManagement.createLobby("TestLobby", firstOwner);
        }, "Should throw an exception when lobby name already exists.");
        assertEquals("Lobby name TestLobby already exists!", thrown.getMessage());
    }

    @Test
    void dropNonExistentLobbyThrowsExceptionTest() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            lobbyManagement.dropLobby("NonExistentLobby");
        }, "Should throw an exception when trying to delete a non-existent lobby.");
        assertEquals("ILobby name NonExistentLobby not found!", thrown.getMessage());
    }

}
