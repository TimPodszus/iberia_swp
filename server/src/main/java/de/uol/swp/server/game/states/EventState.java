package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;

public class EventState implements IGameState {
    public void handleAction(IGame game, IPlayer player) {
        //Event ausführen

        game.setState(game.getPreviousState());
    }
}
