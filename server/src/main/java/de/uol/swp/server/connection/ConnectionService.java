package de.uol.swp.server.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.request.BuildableTrainTracksRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ConnectionService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(ConnectionService.class);

    IConnectionManagement connectionManagement;

    /**
     * Constructor
     *
     * @param bus                  the EvenBus used throughout the server
     * @param connectionManagement the ConnectionManagement used to handle the connections
     * @since 2019-10-08
     */
    @Inject
    public ConnectionService(EventBus bus, IConnectionManagement connectionManagement) {
        super(bus);
        this.connectionManagement = connectionManagement;
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
        Map<ICity, List<ICard>> availableDestinations =
                connectionManagement.getAvailableDestinations(request.getLobbyId(),
                request.getCityId()
        );
        Map<Integer, List<ICardDTO>> availableDestinationsAsDtos = new HashMap<>();

        for (Map.Entry<ICity, List<ICard>> entry : availableDestinations.entrySet()) {
            List<ICardDTO> cards = entry.getValue()
                                        .stream()
                                        .map(CardMapper::toDTO)
                                        .toList();
            availableDestinationsAsDtos.put(entry.getKey()
                                                 .getId(), cards);
        }

        AvailableDestinationsResponse response = new AvailableDestinationsResponse(availableDestinationsAsDtos);

        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        request.getSession()
               .ifPresent(response::setSession);

        post(response);
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
