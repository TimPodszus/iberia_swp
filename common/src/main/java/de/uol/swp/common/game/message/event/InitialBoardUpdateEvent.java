package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.message.AbstractGameEvent;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;

import java.util.Objects;
@Getter
public class InitialBoardUpdateEvent extends AbstractGameEvent {

    private final int cityId;
    private final IPlayerDTO playerDTO;

    public InitialBoardUpdateEvent(String lobbyCode, int cityId, IPlayerDTO playerDTO) {
        super(lobbyCode);
        this.cityId = cityId;
        this.playerDTO = playerDTO;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InitialBoardUpdateEvent that = (InitialBoardUpdateEvent) o;
        return cityId == that.cityId && Objects.equals(playerDTO, that.playerDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cityId, playerDTO);
    }

}
