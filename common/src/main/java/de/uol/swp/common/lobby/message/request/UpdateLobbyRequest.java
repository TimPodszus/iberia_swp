package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.IUserDTO;
import lombok.Getter;

@Getter
public class UpdateLobbyRequest extends AbstractLobbyRequest {

    private final ILobbyDTO lobbyDTO;

    public UpdateLobbyRequest(ILobbyDTO lobbyDTO, IUserDTO userDto) {
        super(lobbyDTO.getLobbyId(), userDto);
        this.lobbyDTO = lobbyDTO;
    }

}
