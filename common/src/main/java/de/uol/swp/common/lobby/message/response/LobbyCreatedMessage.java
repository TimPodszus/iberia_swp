package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.message.AbstractLobbyMessage;
import de.uol.swp.common.user.IUserDTO;

public class LobbyCreatedMessage extends AbstractLobbyMessage {

    public LobbyCreatedMessage(String lobbyCode, IUserDTO user) {
        super(lobbyCode, user);
    }

}
