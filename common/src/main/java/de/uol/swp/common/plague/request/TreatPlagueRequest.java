package de.uol.swp.common.plague.request;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TreatPlagueRequest {

    private final String lobbyId;
    private final int cityId;
    private final PlagueName plagueName;
}
