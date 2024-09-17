package de.uol.swp.server.game;

import de.uol.swp.common.enums.Action;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
@Getter
public class GameController {
    private static final Logger LOG = LogManager.getLogger(GameController.class);
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameTurn currentTurn;

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
        LOG.info("Game startet mit " + players.size() + " Spielern.");
        nextTurn();
    }

    public void nextTurn() throws InterruptedException {
        if (isGameOver()) {
            endGame();
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        LOG.info(currentPlayer.getUser()
                                        .getUsername() + " ist nun am Zug!");

        currentTurn = new GameTurn(currentPlayer, board);
        currentTurn.startTurn();
        finishTurn(currentPlayer);
    }

    public void finishTurn(Player currentPlayer) throws InterruptedException {
        LOG.info(currentPlayer.getUser()
                                        .getUsername() + " hat seinen Zug beendet.");

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        nextTurn();
    }

    public boolean isGameOver() {
        //Verarbeiten wann das Spiel vorbei ist.
        return false;
    }

    public void endGame() {
        LOG.info("Game over!");
        // Logik zum Beenden des Spiels, z. B. Spieler benachrichtigen, Ergebnisse speichern, etc.
    }

    public void receiveActionMessage(User user, Action action) {
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
