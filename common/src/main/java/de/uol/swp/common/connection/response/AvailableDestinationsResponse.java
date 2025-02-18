package de.uol.swp.common.connection.response;


import java.util.List;
import java.util.Map;

import de.uol.swp.common.cards.data.ICardDTO;
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
     * Value is a list of cards, that can be discarded to access the city. If the list is empty, the city is accessible without discarding a card.
     */
    private final Map<Integer, List<ICardDTO>> cities;
}
