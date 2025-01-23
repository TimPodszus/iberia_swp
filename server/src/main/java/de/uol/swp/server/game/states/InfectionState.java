package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;

/**
 * Represents the state in the game where cities are infected based on drawn infection cards.
 * This state manages the infection process, specifically overseeing the spread of infection
 * to cities during a player's turn. It continues infecting cities until the number of infected
 * cities matches the game's current infection rate, at which point it transitions the game state
 * to the next player's turn.
 */
public class InfectionState implements IGameState {
    private int infectedCities = 0;

    /**
     * Executes the infection process for a single turn by infecting cities based on infection cards.
     * This method draws an infection card, uses it to infect a city, and increments the count of infected cities.
     * When the number of cities infected during the turn equals the game's infection rate (infectionCounter),
     * the game transitions to the next player by updating the currentPlayerIndex and sets the game state to
     * PlayerTurnState, preparing for the next player's actions.
     *
     * @param game   the game context in which the infection is being handled
     * @param player the player whose turn initiated the infection process
     */
    public void handleAction(IGame game, IPlayer player) {
       //Todo: Ticket zur überarbeitung der GameStates
    }
}
