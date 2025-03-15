package de.uol.swp.common.plague.message.request;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to treat a plague in a specific city within a game session.
 */
@Getter
public class TreatPlagueRequest extends AbstractGameRequest {

    /**
     * The ID of the city where the plague should be treated.
     */
    private final int cityId;
    /**
     * The name of the plague to be treated.
     */
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
