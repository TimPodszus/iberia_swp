package de.uol.swp.common.plague.response;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TreatPlagueResponse extends AbstractGameResponse {

    private final PlagueName plagueName;
    private final int cityID;

    public TreatPlagueResponse(String lobbyId, boolean success, PlagueName plagueName, int cityID) {
        super(lobbyId, success);
        this.plagueName = plagueName;
        this.cityID = cityID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreatPlagueResponse that)) return false;
        if (!super.equals(o)) return false;
        return getCityID() == that.getCityID() && getPlagueName() == that.getPlagueName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPlagueName(), getCityID());
    }
}

