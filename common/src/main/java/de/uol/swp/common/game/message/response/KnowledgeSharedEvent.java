package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.message.event.AbstractEventMessage;
import lombok.Getter;

/**
 * Event message indicating that knowledge has been shared in the game.
 */
@Getter
public class KnowledgeSharedEvent extends AbstractEventMessage {
    String lobbyId;
    boolean success;
    IGameDTO gameDTO;

    /**
     * Constructs a new KnowledgeSharedEvent.
     *
     * @param lobbyId the ID of the lobby
     * @param success whether the knowledge sharing was successful
     * @param gameDTO the game data transfer object
     */
    public KnowledgeSharedEvent(String lobbyId, boolean success, IGameDTO gameDTO) {
        this.lobbyId = lobbyId;
        this.success = success;
        this.gameDTO = gameDTO;
    }

    /**
     * Checks if the knowledge sharing was successful.
     *
     * @return true if the knowledge sharing was successful, false otherwise
     */
    public boolean wasSuccessful() {
        return success;
    }
}