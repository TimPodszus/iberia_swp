package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MovePlayerRequest extends AbstractGameRequest {
    private final int cityId;

    /**
     * Constructs a new MovePlayerRequest.
     *
     * @param lobbyCode the code of the lobby
     * @param cityId    the ID of the city
     */
    public MovePlayerRequest(String lobbyCode, int cityId) {
        super(lobbyCode);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MovePlayerRequest that = (MovePlayerRequest) object;
        return Objects.equals(super.getLobbyId(), that.getLobbyId()) && Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), cityId);
    }
}
