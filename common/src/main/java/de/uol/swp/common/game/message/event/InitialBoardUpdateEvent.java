package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

import java.util.Objects;
@Getter
public class InitialBoardUpdateEvent extends AbstractGameEvent {

    private final IGameDTO GameDTO;

    public InitialBoardUpdateEvent(String lobbyCode, IGameDTO game) {
        super(lobbyCode);
        this.GameDTO = game;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InitialBoardUpdateEvent that = (InitialBoardUpdateEvent) o;
        return Objects.equals(GameDTO, that.getGameDTO());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), GameDTO);
    }

}
