package de.uol.swp.common.plague;

import lombok.Getter;

public class CanResearchPlagueMessage extends AbstractPlagueMessage {

    @Getter
    boolean success;

    public CanResearchPlagueMessage(boolean success) {
        this.success = success;
    }
}
