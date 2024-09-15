package de.uol.swp.common.message;

import de.uol.swp.common.enums.Action;

public class ActionMessage extends AbstractRequestMessage {
    private final Action action;
    private final String lobbyId;  // Hinzufügen des Lobby-Identifikators

    public ActionMessage(Action action, String lobbyId, MessageContext context) {
        this.action = action;
        this.lobbyId = lobbyId;
        this.setMessageContext(context);
    }

    public Action getAction() {
        return action;
    }

    public String getLobbyId() {
        return lobbyId;
    }
}
