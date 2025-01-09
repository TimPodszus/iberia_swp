package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.IUserDTO;

public class GetLobbyRequest extends AbstractLobbyRequest {

    public GetLobbyRequest(String lobbyCode, IUserDTO userDto) {
        super(lobbyCode, userDto);
    }
}
