package de.uol.swp.server.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.server.AbstractService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

@Singleton
public class ConnectionService extends AbstractService {
    ConnectionManagement connectionManagement;

    /**
     * Constructor
     *
     * @param bus                  the EvenBus used throughout the server
     * @param connectionManagement the ConnectionManagement used to handle the connections
     * @since 2019-10-08
     */
    @Inject
    public ConnectionService(EventBus bus, ConnectionManagement connectionManagement) {
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
        List<ICityDTO> availableDestinations = connectionManagement.getAvailableDestinations(request.getCity());
        AvailableDestinationsResponse response = new AvailableDestinationsResponse(availableDestinations);

        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        request.getSession()
               .ifPresent(response::setSession);

        post(response);
    }
}
