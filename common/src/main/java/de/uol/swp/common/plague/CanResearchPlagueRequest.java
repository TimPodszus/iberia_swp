package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;
import lombok.Getter;

public class CanResearchPlagueRequest extends AbstractPlagueMessage {
    @Getter
    private final String lobbyId;
    public CanResearchPlagueRequest (PlagueName name, String lobbyId) {
        super(name);
        this.lobbyId = lobbyId;
    }

}

