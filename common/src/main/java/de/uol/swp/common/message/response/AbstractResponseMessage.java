package de.uol.swp.common.message.response;

import de.uol.swp.common.message.AbstractMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * Base class of all response messages. Basic handling of answers from the server
 * to the client
 *
 * @see de.uol.swp.common.message.AbstractMessage
 * @see ResponseMessage
 * @author Marco Grawunder
 * @since 2019-08-07
 */
@Getter
public abstract class AbstractResponseMessage extends AbstractMessage implements ResponseMessage {
    private boolean success;
    private String description;

    public AbstractResponseMessage(boolean success, String description) {
        this.success = success;
        this.description = description;
    }
    public AbstractResponseMessage() {
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
        AbstractResponseMessage that = (AbstractResponseMessage) o;
        return success == that.success && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), success, description);
    }
}
