package de.uol.swp.common.message;

import de.uol.swp.common.user.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base class of all server messages. Basic handling of notifications from the server
 * to a group of clients
 *
 * @see de.uol.swp.common.message.AbstractMessage
 * @see de.uol.swp.common.message.ServerMessage
 * @author Marco Grawunder
 * @since 2019-08-07
 */
public class AbstractServerMessage extends AbstractMessage implements ServerMessage {

    private transient List<Session> receiver = new ArrayList<>();

    @Override
    public void setReceiver(List<Session> receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<Session> getReceiver() {
        return receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractServerMessage that = (AbstractServerMessage) o;
        return Objects.equals(receiver, that.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), receiver);
    }
}
