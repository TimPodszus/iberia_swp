package de.uol.swp.common.plague.response;

import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.infection.IInfectionDTO;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AvailablePlaguesResponse that = (AvailablePlaguesResponse) obj;


        return cityId == that.cityId &&
                Objects.equals(availablePlagues, that.availablePlagues) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availablePlagues, role, cityId);
    }
}

