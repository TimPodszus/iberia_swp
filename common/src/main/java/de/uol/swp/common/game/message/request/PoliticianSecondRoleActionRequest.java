package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

public class PoliticianSecondRoleActionRequest extends AbstractGameRequest {

    public PoliticianSecondRoleActionRequest(String lobbyId) {
        super(lobbyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PoliticianSecondRoleActionRequest that = (PoliticianSecondRoleActionRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}
