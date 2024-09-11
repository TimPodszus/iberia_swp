package de.uol.swp.server;

import de.uol.swp.common.enums.Action;
import de.uol.swp.server.gameturn.GameTurn;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;
import lombok.Getter;

import java.util.List;

public class GameController
{
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameTurn currentTurn;

    public GameController(Board board, List<Player> players)
    {
        this.board = board;
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public void startGame()
    {
        System.out.println("Game started with " + players.size() + " players.");
        nextTurn();
    }

    public void receiveActionMessage(Player player, Action action) {
        // Stelle sicher, dass der richtige Spieler am Zug ist
        if (players.get(currentPlayerIndex).equals(player)) {
            processPlayerAction(action);
            // Verarbeite weitere Logik oder sende Antwortnachrichten
        }
    }

    public void processPlayerAction(Action action)
    {
        currentTurn.processAction(action);
    }

    public void nextTurn()
    {
        if (isGameOver()) {
            endGame();
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayer.getUsername() + " ist nun am Zug!");

        currentTurn = new GameTurn(currentPlayer, board);
        currentTurn.startTurn();
        finishTurn(currentPlayer);
    }

    public void finishTurn(Player currentPlayer)
    {
        System.out.println(currentPlayer.getUsername() + " hat seinen Zug beendet.");

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        nextTurn();
    }

    private boolean isGameOver()
    {
        return false;
    }

    private void endGame()
    {
        System.out.println("Game over!");
        // Logik zum Beenden des Spiels, z. B. Spieler benachrichtigen, Ergebnisse speichern, etc.
    }

}
