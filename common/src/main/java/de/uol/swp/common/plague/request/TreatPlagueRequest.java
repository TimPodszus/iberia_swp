package de.uol.swp.common.plague.request;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TreatPlagueRequest extends AbstractGameRequest {

    private final int cityId;
    private final PlagueName plagueName;

    public TreatPlagueRequest(String lobbyId, int cityId, PlagueName plagueName) {
        super(lobbyId);
        this.cityId = cityId;
        this.plagueName = plagueName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreatPlagueRequest that)) {
            return false;
        }
        return getCityId() == that.getCityId() && getPlagueName() == that.getPlagueName() && getLobbyId().equals(that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), getCityId(), getPlagueName());
    }
}
