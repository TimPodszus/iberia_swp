package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;

/**
 * A request message sent to research a specific plague.
 * This class represents a message that is sent when a player
 * requests to research a particular plague. It contains information about
 * the plague being researched, such as its name.
 */
public class ResearchPlagueRequest extends AbstractPlagueMessage {

    public ResearchPlagueRequest() {

    }
    public ResearchPlagueRequest(PlagueName name) {
        super(name);
    }

}
