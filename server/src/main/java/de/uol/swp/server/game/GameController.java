package de.uol.swp.server.game;

import de.uol.swp.common.enums.Action;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;

import java.util.List;
import java.util.Set;

public class GameController {
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameTurn currentTurn;

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public GameTurn getCurrentTurn() {
        return currentTurn;
    }

    public GameController(Lobby lobby) {
        createPlayers(lobby.getUsers());
        this.currentPlayerIndex = 0;
    }

    public void initializeGame() {
        //Logik zur Initiallisierung des Spiels
    }

    private void createPlayers(Set<User> users) {
        for (User user : users) {
            Player player = new Player(user);
            this.players.add(player);
        }
    }

    public void startGame() throws InterruptedException {
        System.out.println("Game startet mit " + players.size() + " Spielern.");
        nextTurn();
    }

    public void nextTurn() throws InterruptedException {
        if (isGameOver()) {
            endGame();
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayer.getUser()
                                        .getUsername() + " ist nun am Zug!");

        currentTurn = new GameTurn(currentPlayer, board);
        currentTurn.startTurn();
        finishTurn(currentPlayer);
    }

    public void finishTurn(Player currentPlayer) throws InterruptedException {
        System.out.println(currentPlayer.getUser()
                                        .getUsername() + " hat seinen Zug beendet.");

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        nextTurn();
    }

    public boolean isGameOver() {
        //Verarbeiten wann das Spiel vorbei ist.
        return false;
    }

    public void endGame() {
        System.out.println("Game over!");
        // Logik zum Beenden des Spiels, z. B. Spieler benachrichtigen, Ergebnisse speichern, etc.
    }

    public void receiveActionMessage(User user, Action action) {
        // Stelle sicher, dass der richtige Spieler am Zug ist
        if (players.get(currentPlayerIndex)
                   .getUser()
                   .equals(user)) {
            processPlayerAction(action);
            // Verarbeite weitere Logik oder sende Antwortnachrichten
        }
    }

    public void processPlayerAction(Action action) {
        currentTurn.processAction(action);
    }
}
