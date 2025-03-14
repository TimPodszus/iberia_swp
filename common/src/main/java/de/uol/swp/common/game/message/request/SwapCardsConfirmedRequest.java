package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.game.message.event.SwapCardsConfirmationEvent;

import java.util.Objects;

public class SwapCardsConfirmedRequest extends AbstractGameRequest {

    SwapCardsConfirmationEvent event;
    boolean accepted;

    public SwapCardsConfirmedRequest(String lobbyId, boolean accepted, SwapCardsConfirmationEvent event) {
        super(lobbyId);
        this.accepted = accepted;
        this.event = event;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SwapCardsConfirmedRequest that = (SwapCardsConfirmedRequest) o;
        return accepted == that.accepted && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, accepted);
    }


}
