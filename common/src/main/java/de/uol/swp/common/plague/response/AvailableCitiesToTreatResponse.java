package de.uol.swp.common.plague.response;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class AvailableCitiesToTreatResponse extends AbstractGameResponse {

    private final List<ICityDTO> availableCities;

    public AvailableCitiesToTreatResponse(String lobbyId, boolean success, List<ICityDTO> availableCities) {
        super(lobbyId, success);
        this.availableCities = availableCities;

    }
}

