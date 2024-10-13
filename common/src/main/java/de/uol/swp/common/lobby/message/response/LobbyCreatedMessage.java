package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.message.AbstractLobbyMessage;
import de.uol.swp.common.user.User;

public class LobbyCreatedMessage extends AbstractLobbyMessage {

    public LobbyCreatedMessage(String lobbyCode, User user) {
        super(lobbyCode, user);
    }

}
