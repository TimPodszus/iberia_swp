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
     * @param lobbyCode the lobby code of the game for which available destinations are to be requested
     * @param cityId    the ID of the city for which available destinations are to be requested
     */
    public void requestAvailableDestination(String lobbyCode, String cityId) {
        AvailableDestinationsRequest request = new AvailableDestinationsRequest(lobbyCode, cityId);
        eventBus.post(request);
    }

    /**
     * Moves the player to the specified city.
     *
     * @param cityDTO the city to which the player is to be moved
     */
    public void movePlayerToCity(ICityDTO cityDTO) {
        // TODO: FIx this
    }
}
