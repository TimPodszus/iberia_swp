package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

import java.util.Objects;

@Getter
public class EndGameEvent extends AbstractGameEvent {
    private final boolean isVictory;
    /**
     * Constructs a new AbstractGameEvent.
     *
     * @param lobbyId the code of the lobby associated with the event
     */
    public EndGameEvent(String lobbyId, boolean isVictory) {
        super(lobbyId);
        this.isVictory = isVictory;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EndGameEvent that = (EndGameEvent) o;
        return isVictory() == that.isVictory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isVictory());
    }
}
