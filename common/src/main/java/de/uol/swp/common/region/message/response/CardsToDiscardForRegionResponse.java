package de.uol.swp.common.region.message.response;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;
import java.util.List;
import java.util.Objects;

/**
 * A response containing the city cards that a player has to discard for a region.
 */
@Getter
public class CardsToDiscardForRegionResponse extends AbstractGameResponse {
    private final List<CityCardDTO> cityCards;

    /**
     * Constructs a new CardsToDiscardForRegionResponse.
     *
     * @param lobbyId   the ID of the lobby
     * @param cityCards the list of city cards to discard
     */
    public CardsToDiscardForRegionResponse(String lobbyId, List<CityCardDTO> cityCards) {
        super(lobbyId, true, "");
        this.cityCards = cityCards;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CardsToDiscardForRegionResponse that = (CardsToDiscardForRegionResponse) o;
        return Objects.equals(getCityCards(), that.getCityCards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCityCards());
    }
}
