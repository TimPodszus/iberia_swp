package de.uol.swp.common.connection.response;


import java.util.List;
import java.util.Map;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

/**
 * Response message containing a list of available destination cities.
 */
@Getter
public class AvailableDestinationsResponse extends AbstractGameResponse {

    /**
     * Constructs a new AvailableDestinationsResponse.
     *
     * @param lobbyId the ID of the lobby
     * @param cities  a map of city IDs to lists of cards that can be discarded to access the city
     */
    public AvailableDestinationsResponse(String lobbyId, Map<Integer, List<ICardDTO>> cities) {
        super(lobbyId, true, "");
        this.cities = cities;
    }

    /**
     * Map of cities and if they are only accessible when the player discards a city card.
     * <p>
     * Key is the city id.
     * Value is a list of cards, that can be discarded to access the city. If the list is empty, the city is accessible without discarding a card.
     */
    private final Map<Integer, List<ICardDTO>> cities;
}
