package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Base class of all lobby request messages. Basic handling of lobby data.
 *
 * @author Marco Grawunder
 * @see AbstractRequestMessage
 * @since 2019-10-08
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractLobbyRequest extends AbstractRequestMessage {

    String lobbyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractLobbyRequest that = (AbstractLobbyRequest) o;
        return Objects.equals(lobbyId, that.lobbyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId);
    }
}
