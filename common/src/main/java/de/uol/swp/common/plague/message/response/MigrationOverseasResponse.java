package de.uol.swp.common.plague.message.response;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class MigrationOverseasResponse extends AbstractGameResponse {
    List<ICityDTO> availableCities;

    /**
     * Response containing the available cities for migration in a specific city within a game session.
     *
     * @param lobbyId         The ID of the lobby.
     * @param availableCities The list of available cities for migration.
     */
    public MigrationOverseasResponse(String lobbyId, boolean success, List<ICityDTO> availableCities) {
        super(lobbyId, success, "");
        this.availableCities = availableCities;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MigrationOverseasResponse that = (MigrationOverseasResponse) o;
        return Objects.equals(getAvailableCities(), that.getAvailableCities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAvailableCities());
    }
}
