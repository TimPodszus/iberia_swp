package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class MovePlayerRequest extends AbstractGameRequest {
    private final String username;
    private final int cityId;
    private final int cardId;

    /**
     * Constructs a new MovePlayerRequest with no card to discard and no user to take with.
     *
     * @param lobbyCode the code of the lobby
     * @param cityId    the ID of the city
     */
    public MovePlayerRequest(String lobbyCode, int cityId) {
        super(lobbyCode);
        this.cityId = cityId;
        this.cardId = -1;
        this.username = "";
    }

    /**
     * Constructs a new MovePlayerRequest with no user to take with.
     *
     * @param lobbyCode the code of the lobby
     * @param cityId    the ID of the city
     */
    public MovePlayerRequest(String lobbyCode, int cityId, int cardId) {
        super(lobbyCode);
        this.cityId = cityId;
        this.cardId = cardId;
        this.username = "";
    }

    /**
     * Constructs a new MovePlayerRequest.
     *
     * @param lobbyCode the code of the lobby
     * @param cityId    the ID of the city
     * @param cardId    the ID of the card
     * @param username  the username of the player, which should be taken with
     */
    public MovePlayerRequest(String lobbyCode, int cityId, int cardId, String username) {
        super(lobbyCode);
        this.cityId = cityId;
        this.cardId = cardId;
        this.username = username;
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
        return Objects.equals(super.getLobbyId(), that.getLobbyId()) && Objects.equals(cityId,
                that.cityId
        ) && Objects.equals(cardId, that.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), cityId, cardId);
    }
}
