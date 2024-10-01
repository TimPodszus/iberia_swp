package de.uol.swp.common.lobby.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.User;

public class GetLobbyRequest extends AbstractLobbyRequest {

    public GetLobbyRequest(String lobbyCode, User userDto) {
        super(lobbyCode, userDto);
    }
}
