package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;

public class ResearchPlagueRequest extends AbstractPlagueMessage {

    public ResearchPlagueRequest() {

    }

    public ResearchPlagueRequest(PlagueName name) {
        super(name);
    }

}
