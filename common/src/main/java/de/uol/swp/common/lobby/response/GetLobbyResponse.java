package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetLobbyResponse extends AbstractResponseMessage {
    private final ILobbyDTO lobbyDTO;
}
