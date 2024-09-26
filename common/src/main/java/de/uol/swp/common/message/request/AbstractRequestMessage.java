package de.uol.swp.common.message.request;

import de.uol.swp.common.message.AbstractMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * Base class of all request messages. Basic handling of messages from the client
 * to the server
 *
 * @see de.uol.swp.common.message.AbstractMessage
 * @see RequestMessage
 * @author Marco Grawunder
 * @since 2019-08-07
 */
@Getter
public abstract class AbstractRequestMessage extends AbstractMessage implements RequestMessage {
    private String lobbyCode;

    protected AbstractRequestMessage(){
    }

    public AbstractRequestMessage(String lobbyCode){
        this.lobbyCode = lobbyCode;
    }
    @Override
    public boolean authorizationNeeded() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AbstractRequestMessage that = (AbstractRequestMessage) o;
        return lobbyCode.equals(that.lobbyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyCode);
    }
}
