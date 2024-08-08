package de.uol.swp.common.message;

/**
 * Base class of all request messages. Basic handling of messages from the client
 * to the server
 *
 * @see de.uol.swp.common.message.AbstractMessage
 * @see de.uol.swp.common.message.RequestMessage
 * @author Marco Grawunder
 * @since 2019-08-07
 */
public abstract class AbstractRequestMessage extends AbstractMessage implements RequestMessage {

    @Override
    public boolean authorizationNeeded() {
        return true;
    }
}
