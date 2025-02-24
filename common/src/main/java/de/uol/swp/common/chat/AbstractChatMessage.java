package de.uol.swp.common.chat;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractChatMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getSender(), that.getSender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyId(), getMessage(), getSender());
    }
}
