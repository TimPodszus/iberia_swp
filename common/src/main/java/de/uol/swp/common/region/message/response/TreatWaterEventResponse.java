package de.uol.swp.common.region.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TreatWaterEventResponse extends AbstractGameResponse {
    boolean isDismissible;

    public TreatWaterEventResponse(String lobbyId, boolean isDismissible) {
        super(lobbyId, true, "");
        this.isDismissible = isDismissible;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreatWaterEventResponse that = (TreatWaterEventResponse) o;
        return super.equals(that) && isDismissible == that.isDismissible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isDismissible);
    }
}
