package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BoardUpdateMessage extends AbstractGameMessage {
    private final IGameDTO gameDTO;

    public BoardUpdateMessage(IGameDTO gameDTO) {
        super(gameDTO.getGameId());
        this.gameDTO = gameDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardUpdateMessage that = (BoardUpdateMessage) o;
        return Objects.equals(gameDTO, that.gameDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDTO);
    }
}
