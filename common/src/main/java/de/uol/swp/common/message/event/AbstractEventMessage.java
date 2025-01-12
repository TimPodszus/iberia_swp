package de.uol.swp.common.message.event;

import de.uol.swp.common.message.AbstractServerMessage;

/**
 * Abstract base class for event messages.
 * <p>
 * This class serves as a base for all event messages that are sent from the server.
 * It extends the AbstractServerMessage class and implements the EventMessage interface.
 */
public abstract class AbstractEventMessage extends AbstractServerMessage implements EventMessage {
}