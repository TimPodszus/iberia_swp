package de.uol.swp.common.lobby.message.event;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LobbyClosedEvent extends AbstractServerMessage {
    private String lobbyId;
}
