package de.uol.swp.server.message;

import de.uol.swp.server.usermanagement.IUser;
import lombok.Getter;

import java.util.Objects;

/**
 * This message is used if a successful login occurred
 * This message is used to signalize all Services it is relevant to, that someone
 * just logged in successfully
 *
 * @see de.uol.swp.server.message.AbstractServerInternalMessage
 * @see de.uol.swp.server.usermanagement.AuthenticationService
 * @author Marco Grawunder
 * @since 2019-08-07
 */
@Getter
public class ClientAuthorizedMessage extends AbstractServerInternalMessage {

    private final IUser user;

    /**
     * Constructor
     *
     * @param user user whose client authorized successfully
     * @see IUser
     * @since 2019-08-07
     */
    public ClientAuthorizedMessage(IUser user) {
        super();
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientAuthorizedMessage that = (ClientAuthorizedMessage) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}
