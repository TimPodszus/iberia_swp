package de.uol.swp.common.game.message.response;


import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.message.event.AbstractEventMessage;
import lombok.Getter;

@Getter
public class KnowledgeSharedEvent extends AbstractEventMessage {
    String lobbyId;
    boolean success;
    IGameDTO gameDTO;

    public KnowledgeSharedEvent(String lobbyId, boolean success, IGameDTO gameDTO) {
        this.lobbyId = lobbyId;
        this.success = success;
        this.gameDTO = gameDTO;
    }

    public boolean wasSuccessful() {
        return success;
    }

}
