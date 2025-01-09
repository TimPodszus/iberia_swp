package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;

public class StartGameEvent extends AbstractGameEvent {

    public StartGameEvent(String lobbyCode, IGameDTO game) {
        super(lobbyCode, game);
    }
}
