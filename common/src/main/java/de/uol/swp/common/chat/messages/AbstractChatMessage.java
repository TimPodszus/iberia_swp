package de.uol.swp.common.chat.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class AbstractChatMessage extends AbstractServerMessage {
    String lobbyId;

    String message;

    protected AbstractChatMessage(String lobbyId, String message) {
        this.lobbyId = lobbyId;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractChatMessage that)) return false;
        return super.equals(that) && Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(getMessage(),
                that.getMessage()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyId(), getMessage());
    }
}
