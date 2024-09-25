package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.message.AbstractResponseMessage;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LobbyListMessage extends AbstractResponseMessage {
    private final Map<String, ILobby> lobbies = new HashMap<>();

    public LobbyListMessage(Map<String, ILobby> lobbies) {
        this.lobbies.putAll(lobbies);
    }
}
