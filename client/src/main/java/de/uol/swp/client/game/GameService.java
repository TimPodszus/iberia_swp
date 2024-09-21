package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import org.greenrobot.eventbus.EventBus;

/**
 * Service class for handling game-related operations.
 */
public class GameService {
    private final EventBus eventBus;

    /**
     * Constructs a GameService with the specified EventBus.
     *
     * @param eventBus the EventBus to be used for event posting
     */
    @Inject
    public GameService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Requests available destinations for the specified city.
     *
     * @param city the city for which available destinations are requested
     */
    public void requestAvailableDestination(ICityDTO city) {
        AvailableDestinationsRequest request = new AvailableDestinationsRequest(city);
        eventBus.post(request);
    }
}
