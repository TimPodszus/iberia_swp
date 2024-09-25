package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.message.AbstractResponseMessage;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LobbyListResponse extends AbstractResponseMessage {
    private final Map<String, ILobby> lobbies = new HashMap<>();

    public LobbyListResponse(Map<String, ILobby> lobbies) {
        this.lobbies.putAll(lobbies);
    }
}
