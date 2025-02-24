package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.game.data.IGame;
import lombok.Getter;

/**
 * Represents the state in the game where cities are infected based on drawn infection cards.
 * This state manages the infection process, specifically overseeing the spread of infection
 * to cities during a player's turn. It continues infecting cities until the number of infected
 * cities matches the game's current infection rate, at which point it transitions the game state
 * to the next player's turn.
 */
@Getter
public class InfectionState implements IGameState {
    private int infectedCities = 0;
    public StateType getStateType() {
        return StateType.INFECTION_STATE;
    }

    public void increaseInfectedCities(IGame game) {
        infectedCities++;
        int citiesToInfect = switch (game.getInfectionCounter()) {
            case 4, 5 -> 3;
            case 6, 7 -> 4;
            default -> 2;
        };
        if (citiesToInfect == infectedCities) {
            game.setState(new PlayerTurnState());
            int currentPlayerIndex = game.getCurrentPlayerIndex();
            int nextPlayerIndex = currentPlayerIndex == game.getPlayers().size() - 1 ? 0 : currentPlayerIndex + 1;
            game.setCurrentPlayerIndex(nextPlayerIndex);
        }
    }
}