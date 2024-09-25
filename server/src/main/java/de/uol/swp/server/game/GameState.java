package de.uol.swp.server.game;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.game.action.MoveAction;
import de.uol.swp.server.player.Player;

interface GameState {
    void handleAction(GameController controller, Action action, Player player);
}

class StartState implements GameState {
    public void handleAction(GameController controller, Action action, Player player) {
        controller.initializeGame();
        controller.setState(new waitForPositioning());
    }
}

class waitForPositioning implements GameState {
    private int positionedPlayersCount = 0;

    public void handleAction(GameController controller, Action action, Player player) {
        if (action instanceof MoveAction moveAction) {
            try {
                player.setStartingPosition(moveAction.getDestination()
                                                     .getName());
                positionedPlayersCount++;
            } catch (Exception e) {
                // Keine Valide Stadt Message an den User
            }
        }
        if (positionedPlayersCount == controller.getPlayers()
                                                .size()) {
            controller.setCurrentTurn(new GameTurn(controller.getPlayers()
                                                             .get(controller.getCurrentPlayerIndex()),
                    controller.getBoard()
            ));
            controller.setState(new PlayerTurnState());
        }
    }
}

class PlayerTurnState implements GameState {
    public void handleAction(GameController controller, Action action, Player player) {
        controller.getCurrentTurn()
                  .processAction(action);

        if (controller.getCurrentTurn()
                      .isTurnOver()) {
            controller.setState(new DrawCardState());
        }
    }
}

class DrawCardState implements GameState {
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

class InfectionState implements GameState {
    private int infectedCities = 0;

    public void handleAction(GameController controller, Action action, Player player) {
        int infectionCounter = controller.getBoard()
                                         .getInfectionCounter();
        GameTurn currentTurn = controller.getCurrentTurn();

        currentTurn.infectCity(currentTurn.drawInfectionCard(), 1);
        infectedCities++;

        if (infectedCities == infectionCounter) {

            controller.setCurrentTurn(new GameTurn(controller.getPlayers()
                                                             .get(controller.getCurrentPlayerIndex()),
                    controller.getBoard()
            ));
            controller.setState(new PlayerTurnState());
        }
    }
}

class EventState implements GameState {
    public void handleAction(GameController controller, Action action, Player player) {
        //Event ausführen

        controller.setState(controller.getPreviousState());
    }
}

class EndGameState implements GameState {
    public void handleAction(GameController controller, Action action, Player player) {
        // Implementiere das Spielende
    }
}

