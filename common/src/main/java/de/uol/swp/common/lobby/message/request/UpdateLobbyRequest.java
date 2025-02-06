package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import lombok.Getter;

/**
 * Request to update a lobby.
 */
@Getter
public class UpdateLobbyRequest extends AbstractLobbyRequest {

    /**
     * The lobby data transfer object.
     */
    private final ILobbyDTO lobbyDTO;

    /**
     * Constructs a new UpdateLobbyRequest.
     *
     * @param lobbyDTO the lobby data transfer object
     */
    public UpdateLobbyRequest(ILobbyDTO lobbyDTO) {
        super(lobbyDTO.getLobbyId());
        this.lobbyDTO = lobbyDTO;
    }

}
