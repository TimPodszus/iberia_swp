package de.uol.swp.common.message.request;

import de.uol.swp.common.game.Action;
import de.uol.swp.common.message.MessageContext;
import lombok.Getter;
import java.io.Serializable;

@Getter
public abstract class ActionMessage extends AbstractRequestMessage implements Serializable {
    private final Action action;
    private final String lobbyId;

    protected ActionMessage(Action action, String lobbyId, MessageContext context) {
        this.lobbyId = lobbyId;
        this.action = action;
        this.setMessageContext(context);
    }

}
