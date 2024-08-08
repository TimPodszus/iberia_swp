package de.uol.swp.common.user.exception;

import de.uol.swp.common.message.AbstractResponseMessage;

import java.util.Objects;

/**
 * This exception is thrown if something went wrong during the registration process.
 * e.g.: The username is already taken
 *
 * @author Marco Grawunder
 * @since 2019-09-02
 */
public class RegistrationExceptionMessage extends AbstractResponseMessage {

    private final String message;

    /**
     * Constructor
     *
     * @param message String containing the reason why the registration failed
     * @since 2019-09-02
     */
    public RegistrationExceptionMessage(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return "RegistrationExceptionMessage "+message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationExceptionMessage that = (RegistrationExceptionMessage) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
