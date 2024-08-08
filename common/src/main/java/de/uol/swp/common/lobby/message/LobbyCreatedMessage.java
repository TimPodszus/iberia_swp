package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

public class LobbyCreatedMessage extends AbstractLobbyMessage {

    public LobbyCreatedMessage(String name, UserDTO user) {
        super(name, user);
    }

}
