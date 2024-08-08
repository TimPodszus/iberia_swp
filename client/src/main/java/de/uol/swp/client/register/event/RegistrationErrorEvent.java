package de.uol.swp.client.register.event;

/**
 * Event used to show the RegistrationError alert
 *
 * In order to show the RegistrationError alert using this event, post an instance of it
 * onto the eventBus the SceneManager is subscribed to.
 *
 * @author Marco Grawunder
 * @see de.uol.swp.client.SceneManager
 * @since 2019-09-03
 *
 */
public class RegistrationErrorEvent {
    private final String message;

    /**
     * Constructor
     *
     * @param message Message containing the cause of the Error
     * @since 2019-09-03
     */
    public RegistrationErrorEvent(String message) {
        this.message = message;
    }

    /**
     * Gets the error message
     *
     * @return A String containing the error message
     * @since 2019-09-03
     */
    public String getMessage() {
        return message;
    }
}
