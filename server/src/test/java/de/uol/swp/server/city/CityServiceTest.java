package de.uol.swp.server.city;

import de.uol.swp.common.city.request.BuildHospitalRequest;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class CityServiceTest extends EventBusBasedTest {
    private static final String LOBBY_CODE = "testLobbyId";
    private static final int GAME_ID = 1;

    @Mock
    private ICityManagement cityManagement;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Spy
    @InjectMocks
    CityService cityService = new CityService(getBus(), cityManagement, gameManagement, lobbyManagement);

    @Test
    void testOnBuildHospitalRequest() {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);

        BuildHospitalRequest request = new BuildHospitalRequest(LOBBY_CODE, 1);
        request.setSession(session);
    }
}
