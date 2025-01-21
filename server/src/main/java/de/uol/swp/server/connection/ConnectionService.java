package de.uol.swp.server.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.connection.management.IConnectionManagement;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class ConnectionService extends AbstractService {
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
        Map<ICityDTO, Boolean> availableDestinations = connectionManagement.getAvailableDestinations(request.getLobbyId(),
                                                                                   request.getCityId()
                                                                           )
                                                                           .entrySet()
                                                                           .stream()
                                                                           .collect(Collectors.toMap(
                                                                                   entry -> CityMapper.toDTO(entry.getKey()),
                                                                                   Map.Entry::getValue
                                                                           ));


        AvailableDestinationsResponse response = new AvailableDestinationsResponse(availableDestinations);

        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        request.getSession()
               .ifPresent(response::setSession);

        post(response);
    }
}
