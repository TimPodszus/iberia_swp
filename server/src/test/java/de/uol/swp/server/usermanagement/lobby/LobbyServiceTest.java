package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.message.LobbyLeaveUserRequest;

import de.uol.swp.common.user.UserDTO;

import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.lobby.LobbyService;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LobbyServiceTest extends EventBusBasedTest
{

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    static final UserDTO secondOwner = new UserDTO("Marco2", "Marco2", "Marco2@Grawunder.com");


    // Special version of event bus for testing
    final EventBus bus = EventBus.builder()
            .logNoSubscriberMessages(false)
            .sendNoSubscriberEvent(false)
            .throwSubscriberException(true)
            .build();
    @Mock
    private UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    @Mock
    private AuthenticationService authService = new AuthenticationService(bus, userManagement);
    @Mock
    private LobbyManagement lobbyManagement = new LobbyManagement();
    @Mock
    private UserDTO mockUserDTO = new UserDTO("TestUser", "TestPassword", "testemail@gmx.de");
    @InjectMocks
    private LobbyService lobbyService = new LobbyService(lobbyManagement, authService, getBus());

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockUserDTO.getUsername()).thenReturn("TestUser");
        lobbyManagement.createLobby("TestLobby", mockUserDTO);
    }

    @Test
    void createLobbyTest() {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if Lobby was created
        assertNotNull(lobbyManagement.getLobby("Test"));
        // Checks whether it is also the correct owner
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertEquals(lobbyManagement.getLobby("Test").get().getOwner(), firstOwner);
        }
    }

    @Test
    void createSecondLobbyWithSameName() {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);
        final CreateLobbyRequest request2 = new CreateLobbyRequest("Test", secondOwner);

        bus.post(request);

        // event bus throw exception
        Exception e = assertThrows(EventBusException.class,
                () -> bus.post(request2)
        );
        // Check if the nested exception is the right exception
        assertTrue(e.getCause() instanceof IllegalArgumentException);

        // old lobby should be still in the LobbyManagement
        assertNotNull(lobbyManagement.getLobby("Test"));

        // old lobby should not be overwritten!
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertNotEquals(lobbyManagement.getLobby("Test").get().getOwner(), secondOwner);
        }
    }

    @Test
    void lobbyJoinUserTest() {
        // Create the lobby
        lobbyManagement.createLobby("Test", firstOwner);

        final LobbyJoinUserRequest request = new LobbyJoinUserRequest("Test", secondOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if the user is listed in the lobby
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertTrue(lobbyManagement.getLobby("Test").get().getUsers().contains(firstOwner));
            assertTrue(lobbyManagement.getLobby("Test").get().getUsers().contains(secondOwner));
        }
    }

    @Test
    void lobbyLeaveUserTest() {
        // Create the lobby
        lobbyManagement.createLobby("Test", firstOwner);
        // Join User
        if (lobbyManagement.getLobby("Test").isPresent()) {
            lobbyManagement.getLobby("Test").get().joinUser(secondOwner);
        }

        final LobbyLeaveUserRequest request = new LobbyLeaveUserRequest("Test", secondOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if the user is no longer listed in the lobby
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertFalse(lobbyManagement.getLobby("Test").get().getUsers().contains(secondOwner));
        }
    }


    @Test
    void lobbyIsAddedToOpenLobbyListTest() throws InterruptedException {

        // Arrange
        final CreateLobbyRequest request = new CreateLobbyRequest("TestLobby", firstOwner);

        // Act
        postAndWait(request);

        // Assert
        assertTrue(lobbyManagement.getLobby("TestLobby").isPresent(), "Lobby should be added to the list of open lobbies.");
    }

    @Test
    void creatorIsAutomaticallyAddedToLobbyTest() throws InterruptedException {
        // Arrange
        final CreateLobbyRequest request = new CreateLobbyRequest("TestLobby2", firstOwner);

        // Act
        postAndWait(request);

        // Assert
        if (lobbyManagement.getLobby("TestLobby").isPresent()) {
            ILobby createdLobby = lobbyManagement.getLobby("TestLobby").get();
            assertTrue(createdLobby.getUsers().contains(firstOwner), "Lobby creator should automatically be added to the lobby.");
        } else {
            fail("Lobby should be present after creation.");
        }
    }

    @Test
    void lobbyNameAndLobbyCodeAreGeneratedTest() throws InterruptedException {
        // Arrange
        final CreateLobbyRequest request = new CreateLobbyRequest("TestLobby3", firstOwner);

        // Act
        postAndWait(request);

        // Assert
        ILobby createdLobby = lobbyManagement.getLobby("TestLobby").orElse(null);
        assertNotNull(createdLobby, "Lobby should be created.");
        assertEquals("TestLobby", createdLobby.getName(), "Lobby name should match.");
        assertNotNull(createdLobby.getLobbyCode(), "Lobby code should be generated.");
    }

    @Test
    void lobbyCanBeDeletedTest() {
        // Arrange
        lobbyManagement.createLobby("TestLobby", firstOwner);

        // Act
        lobbyManagement.dropLobby("TestLobby");

        // Assert
        assertFalse(lobbyManagement.getLobby("TestLobby").isPresent(), "Lobby should be deleted.");
    }



}
