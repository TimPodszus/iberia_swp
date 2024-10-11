package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Base class of all lobby messages. Basic handling of lobby data.
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.user.User
 * @see AbstractServerMessage
 * @since 2019-10-08
 */
@Getter
@Setter
public class AbstractLobbyMessage extends AbstractServerMessage {

    String lobbyCode;
    User user;

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public AbstractLobbyMessage() {
    }

    /**
     * Constructor
     *
     * @param lobbyCode code of the lobby
     * @param user      user responsible for the creation of this message
     * @since 2019-10-08
     */
    public AbstractLobbyMessage(String lobbyCode, User user) {
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
        AbstractLobbyMessage that = (AbstractLobbyMessage) o;
        return Objects.equals(lobbyCode, that.lobbyCode) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyCode, user);
    }
}
