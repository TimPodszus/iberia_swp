package de.uol.swp.common.plague.response;

import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.infection.IInfectionDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class AvailablePlaguesResponse extends AbstractGameResponse {

    private final List<IInfectionDTO> availablePlagues;
    private final RoleEnum role;
    private final int cityId;

    public AvailablePlaguesResponse(String lobbyId, boolean success, List<IInfectionDTO> availablePlagues, RoleEnum role, int cityId) {
        super(lobbyId, success);
        this.availablePlagues = availablePlagues;
        this.role = role;
        this.cityId = cityId;
    }
}

