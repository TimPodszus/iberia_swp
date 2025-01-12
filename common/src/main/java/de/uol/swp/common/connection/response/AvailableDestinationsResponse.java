package de.uol.swp.common.connection.response;


import java.util.Map;

import de.uol.swp.common.city.ICityDTO;
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
     * Map of cities and if they are only accessible when the player discards the city card.
     * <p>
     * Key is the city DTO.
     * Value is a boolean. True if the city is only accessible when the player discards the city card.
     */
    private final Map<ICityDTO, Boolean> cities;
}
