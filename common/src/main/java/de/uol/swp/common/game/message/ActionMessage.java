package de.uol.swp.common.game.message;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.message.MessageContext;
import lombok.Getter;

@Getter
public class ActionMessage extends AbstractRequestMessage {
    private final Action action;
    private final String lobbyId;  // Hinzufügen des Lobby-Identifikators

    public ActionMessage(Action action, String lobbyId, MessageContext context) {
        this.action = action;
        this.lobbyId = lobbyId;
        this.setMessageContext(context);
    }
}
