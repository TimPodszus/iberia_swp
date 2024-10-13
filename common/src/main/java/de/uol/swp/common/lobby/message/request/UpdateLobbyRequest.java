package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.User;
import lombok.Getter;

@Getter
public class UpdateLobbyRequest extends AbstractLobbyRequest {

    private final ILobbyDTO lobbyDTO;

    public UpdateLobbyRequest(ILobbyDTO lobbyDTO, User userDto) {
        super(lobbyDTO.getLobbyCode(), userDto);
        this.lobbyDTO = lobbyDTO;
    }
}
