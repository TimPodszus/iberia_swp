package de.uol.swp.common.connection.response;

import java.util.Map;

import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

/**
 * Response message containing a list of available destination cities.
 */
@Getter
public class AvailableDestinationsResponse extends AbstractGameResponse {

    /**
     * Map of cities and if they are only accessible when the player discards a city card.
     * <p>
     * Key is the city id.
     * Value is an object, with a list of cards, that can be discarded to access the city and a flag indicating the way
     * to access the city.
     */
    private final Map<Integer, DestinationInfo> cities;

    /**
     * Constructs a new AvailableDestinationsResponse.
     *
     * @param lobbyId the ID of the lobby
     * @param cities  the map of cities and if they are only accessible when the player discards a city card
     */
    public AvailableDestinationsResponse(String lobbyId, Map<Integer, DestinationInfo> cities) {
        super(lobbyId, true, "");
        this.cities = cities;
    }
}
