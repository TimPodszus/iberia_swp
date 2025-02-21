package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;
import lombok.Getter;

public class CanResearchPlagueMessage extends AbstractPlagueMessage{

    @Getter
    boolean success;
    public CanResearchPlagueMessage(PlagueName name, boolean success) {
        super(name);
        this.success = success;
    }
}
