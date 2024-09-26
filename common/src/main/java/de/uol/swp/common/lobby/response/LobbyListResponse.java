package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.message.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LobbyListResponse extends AbstractResponseMessage {
    private final List<ILobby> lobbies;
}
