package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.message.AbstractServerMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event that is triggered when a lobby is updated.
 */
@Getter
@AllArgsConstructor
public class LobbyUpdatedEvent extends AbstractServerMessage {
    /**
     * The Data Transfer Object (DTO) representing the updated lobby.
     */
    private ILobbyDTO lobbyDTO;
}
