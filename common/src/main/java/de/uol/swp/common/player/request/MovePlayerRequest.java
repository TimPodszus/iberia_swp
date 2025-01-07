package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MovePlayerRequest extends AbstractGameRequest {
    private final String playerId;
    private final String cityId;

    /**
     * Constructs a new MovePlayerRequest.
     *
     * @param lobbyCode the code of the lobby
     * @param playerId  the ID of the player
     * @param cityId    the ID of the city
     */
    public MovePlayerRequest(String lobbyCode, String playerId, String cityId) {
        super(lobbyCode);
        this.playerId = playerId;
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
        return Objects.equals(super.getLobbyCode(), that.getLobbyCode()) && Objects.equals(playerId,
                that.playerId
        ) && Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyCode(), playerId, cityId);
    }
}
