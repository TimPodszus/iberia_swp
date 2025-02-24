package de.uol.swp.server.city;

import de.uol.swp.common.city.request.BuildHospitalRequest;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CityServiceTest extends EventBusBasedTest {
    private static final String LOBBY_CODE = "testLobbyId";

    @Mock
    private ICityManagement cityManagement;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Spy
    @InjectMocks
    CityService cityService = new CityService(getBus(), cityManagement, gameManagement, lobbyManagement);

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnBuildHospitalRequest() throws PlayerManagementException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);

        BuildHospitalRequest request = new BuildHospitalRequest(LOBBY_CODE, 1);
        request.setSession(session);

        doNothing().when(cityManagement)
                   .buildHospital(any(String.class), any(String.class), any(Integer.class));

        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame(any(String.class))).thenReturn(game);

        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby(any(String.class))).thenReturn(lobby);

        doNothing().when(cityService)
                   .sendToAllInLobby(any(Lobby.class), any(BoardUpdateEvent.class));

        cityService.onBuildHospitalRequest(request);

        verify(cityManagement, atLeast(1)).buildHospital(any(String.class), any(String.class), any(Integer.class));
    }

    @Test
    void testBuildHospitalRequestEquals() {
        BuildHospitalRequest request1 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request2 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request3 = new BuildHospitalRequest(LOBBY_CODE, 2);
        BuildHospitalRequest request4 = new BuildHospitalRequest("differentLobbyId", 1);

        // Test for the same object
        assertEquals(request1, request1);

        // Test for null
        // Warning: Arguments to 'assertNotEquals()' in wrong order -> needed for full coverage
        assertNotEquals(request1, null);

        // Test for different class
        // Warning: Arguments to 'assertNotEquals()' in wrong order -> needed for full coverage
        assertNotEquals(request1, new Object());

        // Test for equal objects
        assertEquals(request1, request2);

        // Test for different cityId
        assertNotEquals(request1, request3);

        // Test for different lobbyId
        assertNotEquals(request1, request4);
    }

    @Test
    void testBuildHospitalRequestHashCode() {
        BuildHospitalRequest request1 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request2 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request3 = new BuildHospitalRequest(LOBBY_CODE, 2);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}
