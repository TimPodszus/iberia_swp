package de.uol.swp.common.chat;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.Objects;

public class AbstractChatMessage extends AbstractServerMessage {
    String lobbyCode;
    String sender;
    String message;

    public AbstractChatMessage() {

    }

    public AbstractChatMessage(String lobbyCode, String sender, String message) {
        this.lobbyCode = lobbyCode;
        this.sender = sender;
        this.message = message;
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractChatMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getLobbyCode(), that.getLobbyCode()) && Objects.equals(getSender(), that.getSender()) && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyCode(), getSender(), getMessage());
    }
}
