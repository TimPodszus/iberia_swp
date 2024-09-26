package de.uol.swp.common.game.message;

import de.uol.swp.common.game.Action;
import de.uol.swp.common.message.MessageContext;
import lombok.Getter;
import java.util.Objects;

@Getter
public abstract class ActionRequest extends AbstractGameRequest {
    private final Action action;

    protected ActionRequest(Action action, String lobbyCode, MessageContext context) {
        super(lobbyCode);
        this.action = action;
        this.setMessageContext(context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionRequest that = (ActionRequest) o;
        return action.equals(that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }
}
