package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;

import java.util.Objects;

public class BoardUpdateResponse extends AbstractGameResponse {
    private IGameDTO gameDTO;

    public BoardUpdateResponse(boolean success, String description, IGameDTO gameDTO) {
        super(success, description);
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
        if (!super.equals(o)) {
            return false;
        }
        BoardUpdateResponse that = (BoardUpdateResponse) o;
        return Objects.equals(gameDTO, that.gameDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameDTO);
    }
}
