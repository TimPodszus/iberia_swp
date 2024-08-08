package de.uol.swp.server.message;

import java.util.Objects;

/**
 * This message is used if something went wrong
 *
 * This ServerMessage is used if something went wrong e.g. in the login process
 *
 * @see de.uol.swp.server.message.AbstractServerInternalMessage
 * @see de.uol.swp.server.usermanagement.AuthenticationService#onLoginRequest
 * @author Marco Grawunder
 * @since 2019-08-07
 */
public class ServerExceptionMessage extends AbstractServerInternalMessage {

    private final Exception e;

    /**
     * Constructor
     *
     * @param e the Exception that is the reason for the creation of this
     * @since 2019-08-07
     */
    public ServerExceptionMessage(Exception e) {
        super();
        this.e = e;
    }

    /**
     * Getter for the Exception
     *
     * @return Exception passed in the constructor
     * @since 2019-08-07
     */
    public Exception getException(){
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerExceptionMessage that = (ServerExceptionMessage) o;
        return Objects.equals(e, that.e);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), e);
    }
}
