package de.uol.swp.common.message;

import de.uol.swp.common.enums.Action;

/**
 * A concrete message class that carries an action.
 *
 * @see de.uol.swp.common.message.AbstractRequestMessage
 */
public class ActionMessage extends AbstractRequestMessage {
    private final Action action;

    /**
     * Constructs a new ActionMessage with a specific action.
     *
     * @param action the action associated with this message
     */
    public ActionMessage(Action action, MessageContext context) {
        this.action = action;
        this.setMessageContext(context);
    }

    /**
     * Retrieves the action associated with this message.
     *
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    // Override other necessary methods if needed
}
