package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;

import de.uol.swp.common.user.IUserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Base class of all lobby messages. Basic handling of lobby data.
 *
 * @author Marco Grawunder
 * @see IUserDTO
 * @see AbstractServerMessage
 * @since 2019-10-08
 */
@Getter
@Setter
public class AbstractLobbyMessage extends AbstractServerMessage {

    String lobbyId;
    IUserDTO user;

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
     * @param lobbyId code of the lobby
     * @param user    user responsible for the creation of this message
     * @since 2019-10-08
     */
    public AbstractLobbyMessage(String lobbyId, IUserDTO user) {
        this.lobbyId = lobbyId;
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
        return Objects.equals(lobbyId, that.lobbyId) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId, user);
    }
}
