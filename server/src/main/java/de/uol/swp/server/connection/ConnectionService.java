package de.uol.swp.server.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.request.BuildableTrainTracksRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.dto.DestinationInfo;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.events.MovePlayerAnywhereEvent;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.management.ServerUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

@Singleton
public class ConnectionService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(ConnectionService.class);

    IConnectionManagement connectionManagement;

    ServerUserService userManagement;

    /**
     * Constructor
     *
     * @param bus                  the EvenBus used throughout the server
     * @param connectionManagement the ConnectionManagement used to handle the connections
     * @param userManagement       the UserManagement used to handle the users
     * @since 2024-09-20
     */
    @Inject
    public ConnectionService(
            EventBus bus, IConnectionManagement connectionManagement, ServerUserService userManagement
    ) {
        super(bus);
        this.connectionManagement = connectionManagement;
        this.userManagement = userManagement;
    }


    /**
     * Handles the AvailableDestinationsRequest.
     *
     * @param request the request containing the city for which available destinations are needed
     */
    @Subscribe
    public void onAvailableDestinationsRequest(AvailableDestinationsRequest request) {
        LOG.debug("[Lobby: {}] Got AvailableDestinationsRequest for city {}",
                request.getLobbyId(),
                request.getCityId()
        );

        Map<Integer, DestinationInfo> availableDestinations = connectionManagement.getAvailableDestinations(
                request.getLobbyId(),
                request.getCityId()
        );

        AvailableDestinationsResponse response = new AvailableDestinationsResponse(availableDestinations);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        request.getSession()
               .ifPresent(response::setSession);
        post(response);
        LOG.debug("[Lobby: {}] Sent AvailableDestinationsResponse for city {}",
                request.getLobbyId(),
                request.getCityId()
        );
    }

    /**
     * Handles the MovePlayerAnywhereEvent.
     *
     * @param event the event containing the lobby ID and the username of the player to be moved
     * @throws GameException if the user is not logged in
     */
    @Subscribe
    public void onMovePlayerAnywhereEvent(MovePlayerAnywhereEvent event) throws GameException {
        LOG.debug("[Lobby: {}] Got MovePlayerAnywhereEvent for player {}", event.getLobbyId(), event.getUsername());
        IUser user = userManagement.getUser(event.getUsername());
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error("User not logged in");
                                                   return new GameException("User not logged in");
                                               });

        Map<Integer, DestinationInfo> availableDestinations = connectionManagement.getAllDestinations(
                event.getLobbyId()
        );

        AvailableDestinationsResponse response = new AvailableDestinationsResponse(availableDestinations);
        response.setSession(session);
        post(response);
        LOG.debug("[Lobby: {}] Sent AvailableDestinationsResponse for player {}",
                event.getLobbyId(),
                event.getUsername()
        );
    }

    /**
     * Handles the BuildableTrainTracksRequest.
     *
     * @param request the request containing the city for which buildable train tracks are needed
     */
    @Subscribe
    public void onBuildableTrainTracksRequest(BuildableTrainTracksRequest request) {
        LOG.debug("[Lobby: {}] Got BuildableTrainTracksRequest for city {}",
                request.getLobbyId(),
                request.getCityId()
        );
        List<IConnection> connections = connectionManagement.getBuildableTrainTracks(
                request.getLobbyId(),
                request.getCityId()
        );
        BuildableTrainTracksResponse response = new BuildableTrainTracksResponse(
                request.getLobbyId(),
                true,
                ConnectionMapper.toDTOList(connections)
        );

        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        request.getSession()
               .ifPresent(response::setSession);

        post(response);
    }
}
