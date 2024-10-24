package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;

/**
 * A message indicating that a specific plague has been successfully researched.
 * This class represents a message sent after a plague has been fully researched.
 * It contains information about the researched plague, including its name.
 */
public class PlagueResearchedMessage extends AbstractPlagueMessage {

    public PlagueResearchedMessage(PlagueName name) {
        super(name);
    }
}
