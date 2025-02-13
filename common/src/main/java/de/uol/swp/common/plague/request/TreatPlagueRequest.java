package de.uol.swp.common.plague.request;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
