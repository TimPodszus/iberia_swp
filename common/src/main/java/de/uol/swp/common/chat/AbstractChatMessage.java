package de.uol.swp.common.chat;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
public abstract class AbstractChatMessage extends AbstractServerMessage {
    @Setter
    String lobbyCode;

    @Setter
    String message;

    public AbstractChatMessage(String lobbyCode, String message) {
        this.lobbyCode = lobbyCode;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractChatMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getLobbyCode(), that.getLobbyCode()) && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyCode(), getMessage());
    }
}
