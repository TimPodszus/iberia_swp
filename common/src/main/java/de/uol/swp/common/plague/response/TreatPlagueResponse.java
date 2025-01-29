package de.uol.swp.common.plague.response;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

@Getter
public class TreatPlagueResponse extends AbstractGameResponse {

    private final PlagueName plagueName;
    private final int cityID;

    public TreatPlagueResponse(String lobbyId, boolean success, PlagueName plagueName, int cityID) {
        super(lobbyId, success);
        this.plagueName = plagueName;
        this.cityID = cityID;
    }
}

