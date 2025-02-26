package de.uol.swp.common.region.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class WaterTreatmentEventRequest extends AbstractGameRequest {
    private final int regionId;
    private final int amount;
    private final boolean dismissed;

    /**
     * Constructs a new WaterTreatmentRequest with the specified lobby ID, region ID, amount, and card.
     *
     * @param lobbyId  the ID of the lobby
     * @param regionId the ID of the region
     * @param amount   the amount of water treatment
     */
    public WaterTreatmentEventRequest(String lobbyId, int regionId, int amount, boolean dismissed) {
        super(lobbyId);
        this.regionId = regionId;
        this.amount = amount;
        this.dismissed = dismissed;
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
        WaterTreatmentEventRequest that = (WaterTreatmentEventRequest) o;
        return regionId == that.regionId && amount == that.amount && dismissed == that.dismissed && Objects.equals(getLobbyId(), that.getLobbyId());
    }

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), regionId, amount, dismissed);
    }
}