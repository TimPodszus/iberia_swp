package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LobbyListResponse extends AbstractResponseMessage {
    private final List<ILobbyDTO> lobbies;
}
