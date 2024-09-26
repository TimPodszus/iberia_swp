package de.uol.swp.server.game.states;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

public class DrawCardState implements GameState {
    private int cardsDrawn = 0;

    public void handleAction(GameController controller, Action action, Player player) {
        controller.getCurrentTurn()
                  .drawPlayerCard();
        cardsDrawn++;
        if (player.getCards()
                  .size() <= 7 && cardsDrawn == 2) {
            controller.setState(new InfectionState());
        }
    }
}
