package de.uol.swp.server;

import de.uol.swp.common.message.Message;
import de.uol.swp.common.message.ServerMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.Collections;

/**
 * This class is the base for creating a new Service.
 * This class prepares the child classes to have the EventBus set and methods post
 * and sendToAll implemented in order to reduce unnecessary code repetition.
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */


public class AbstractService {

    /**
     * The EventBus instance used for posting and handling events.
     * This is a protected final field, ensuring it is initialized once and cannot be changed.
     */
    protected final EventBus bus;

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     * @since 2019-10-08
     */
    public AbstractService(EventBus bus) {
        this.bus = bus;
        bus.register(this);
    }

    /**
     * Posts a message on the EventBus
     *
     * @param message the message to post
     * @see de.uol.swp.common.message.Message
     * @since 2019-10-08
     */
    protected void post(Message message) {
        bus.post(message);
    }

    /**
     * Prepares a ServerMessage to be sent to all connected users and posts it to the
     * EventBus.
     *
     * @param message the message to be sent to every user
     * @see ServerMessage
     * @since 2019-10-08
     */
    public void sendToAll(ServerMessage message) {
        message.setReceiver(Collections.emptyList());
        post(message);
    }

}
