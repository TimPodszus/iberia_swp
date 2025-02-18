package de.uol.swp.common.connection.response;

import java.util.Map;

import de.uol.swp.common.game.dto.DestinationInfo;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response message containing a list of available destination cities.
 */
@Getter
@AllArgsConstructor
public class AvailableDestinationsResponse extends AbstractResponseMessage {
    /**
     * Map of cities and if they are only accessible when the player discards a city card.
     * <p>
     * Key is the city id.
     * Value is an object, with a list of cards, that can be discarded to access the city and a flag indicating the way
     * to access the city.
     */
    private final Map<Integer, DestinationInfo> cities;
}
