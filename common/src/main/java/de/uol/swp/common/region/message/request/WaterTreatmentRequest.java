package de.uol.swp.common.region.message.request;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;
import java.util.Objects;

/**
 * Request to perform water treatment in a specific region.
 */
@Getter
public class WaterTreatmentRequest extends AbstractGameRequest {
    private final int regionId;
    private final int amount;
    private final CityCardDTO card;

    /**
     * Constructs a new WaterTreatmentRequest with the specified lobby ID, region ID, amount, and card.
     *
     * @param lobbyId  the ID of the lobby
     * @param regionId the ID of the region
     * @param amount   the amount of water treatment
     * @param card     the city card associated with the request
     */
    public WaterTreatmentRequest(String lobbyId, int regionId, int amount, CityCardDTO card) {
        super(lobbyId);
        this.regionId = regionId;
        this.amount = amount;
        this.card = card;
    }

    /**
     * Checks if this request is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterTreatmentRequest that = (WaterTreatmentRequest) o;
        return regionId == that.regionId && amount == that.amount && Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(card, that.card);
    }

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), regionId, amount, card);
    }
}