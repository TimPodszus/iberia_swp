package de.uol.swp.common.chat;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractChatMessage extends AbstractServerMessage {
    @Setter
    String lobbyId;

    @Setter
    String message;

    @Setter
    String sender;

    public AbstractChatMessage(String lobbyId, String message, String sender) {
        this.lobbyId = lobbyId;
        this.message = message;
        this.sender = sender;
    }

}
