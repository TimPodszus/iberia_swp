package de.uol.swp.common.region.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TreatWaterEventResponse extends AbstractResponseMessage {
    boolean isDismissible;

    public TreatWaterEventResponse(boolean isDismissible) {
        this.isDismissible = isDismissible;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TreatWaterEventResponse that = (TreatWaterEventResponse) o;
        return isDismissible == that.isDismissible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isDismissible);
    }
}
