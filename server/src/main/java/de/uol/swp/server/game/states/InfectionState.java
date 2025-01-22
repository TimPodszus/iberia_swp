package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the state in the game where cities are infected based on drawn infection cards.
 * This state manages the infection process, specifically overseeing the spread of infection
 * to cities during a player's turn. It continues infecting cities until the number of infected
 * cities matches the game's current infection rate, at which point it transitions the game state
 * to the next player's turn.
 */
@Getter
@Setter
public class InfectionState implements IGameState {
    private int infectedCities = 0;

    public StateType getStateType() {
        return StateType.INFECTION_STATE;
    }
}
