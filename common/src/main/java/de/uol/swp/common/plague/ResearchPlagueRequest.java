package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;
import lombok.Getter;

/**
 * A request message sent to research a specific plague.
 * This class represents a message that is sent when a player
 * requests to research a particular plague. It contains information about
 * the plague being researched, such as its name.
 */
public class ResearchPlagueRequest extends AbstractPlagueMessage {

    @Getter
    private final String lobbyId;
    public ResearchPlagueRequest(PlagueName name, String lobbyId) {
        super(name);
        this.lobbyId = lobbyId;
    }

}
