package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

/**
 * A request to draw an infection card in the game.
 * This request is sent by a player to draw an infection card from the deck.
 */
public class DrawInfectionCardRequest extends AbstractGameRequest {

    /**
     * Constructs a new DrawInfectionCardRequest with the specified lobby code.
     *
     * @param lobbyCode the code of the lobby where the request is made
     */
    public DrawInfectionCardRequest(String lobbyCode) {
        super(lobbyCode);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * This method always returns false.
     *
     * @param o the reference object with which to compare
     * @return false always
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    /**
     * Returns a hash code value for the object.
     * This method always returns 0.
     *
     * @return 0 always
     */
    @Override
    public int hashCode() {
        return 0;
    }
}