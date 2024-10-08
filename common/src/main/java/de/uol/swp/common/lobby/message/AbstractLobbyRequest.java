package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import de.uol.swp.common.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Base class of all lobby request messages. Basic handling of lobby data.
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.user.User
 * @see AbstractRequestMessage
 * @since 2019-10-08
 */
@Getter
@Setter
public class AbstractLobbyRequest extends AbstractRequestMessage {

    String lobbyCode;

    User user;

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public AbstractLobbyRequest() {
    }

    /**
     * Constructor
     *
     * @param lobbyCode name of the lobby
     * @param user      user responsible for the creation of this message
     * @since 2019-10-08
     */
    public AbstractLobbyRequest(String lobbyCode, User user) {
        this.lobbyCode = lobbyCode;
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractLobbyRequest that = (AbstractLobbyRequest) o;
        return Objects.equals(lobbyCode, that.lobbyCode) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyCode, user);
    }
}
