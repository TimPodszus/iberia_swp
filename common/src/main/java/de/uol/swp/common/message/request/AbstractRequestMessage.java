package de.uol.swp.common.message.request;

import de.uol.swp.common.message.AbstractMessage;
import lombok.Getter;


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

    @Override
    public boolean authorizationNeeded() {
        return true;
    }

}
