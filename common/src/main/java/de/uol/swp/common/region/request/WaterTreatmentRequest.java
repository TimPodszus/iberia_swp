package de.uol.swp.common.region.request;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;
import java.util.Objects;

@Getter
public class WaterTreatmentRequest extends AbstractGameRequest {
    private final int regionId;
    private final int amount;
    private final CityCardDTO card;

    public WaterTreatmentRequest(String lobbyId, int regionId, int amount, CityCardDTO card) {
        super(lobbyId);
        this.regionId = regionId;
        this.amount = amount;
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WaterTreatmentRequest that = (WaterTreatmentRequest) o;
        return getRegionId() == that.getRegionId() && getAmount() == that.getAmount() && Objects.equals(
                getCard(),
                that.getCard()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegionId(), getAmount(), getCard());
    }
}

